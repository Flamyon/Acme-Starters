package acme.features.levelb.jobicy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import acme.forms.JobOffer;

@Service
public class JobicyExchange {

	private static final Logger	LOG		= LoggerFactory.getLogger(JobicyExchange.class);

	private static final String	API_URL	= "https://jobicy.com/api/v2/remote-jobs";

	private final RestTemplate	restTemplate;
	private final boolean		explicitProxyConfigured;
	private volatile boolean	networkHintLogged;


	public JobicyExchange( //
		@Value("${jobicy.http.connect-timeout-ms:10000}") final int connectTimeoutMs, //
		@Value("${jobicy.http.read-timeout-ms:10000}") final int readTimeoutMs, //
		@Value("${jobicy.network.use-system-proxies:true}") final boolean useSystemProxies, //
		@Value("${jobicy.network.prefer-ipv4:true}") final boolean preferIpv4, //
		@Value("${jobicy.proxy.host:}") final String proxyHost, //
		@Value("${jobicy.proxy.port:0}") final int proxyPort, //
		@Value("${jobicy.proxy.user:}") final String proxyUser, //
		@Value("${jobicy.proxy.password:}") final String proxyPassword, //
		@Value("${jobicy.proxy.non-proxy-hosts:}") final String nonProxyHosts //
	) {
		final ProxySettings envProxy = this.extractProxyFromEnvironment();
		this.explicitProxyConfigured = proxyHost != null && !proxyHost.isBlank() || envProxy != null;
		this.networkHintLogged = false;
		this.configureNetworking(useSystemProxies, preferIpv4, proxyHost, proxyPort, proxyUser, proxyPassword, nonProxyHosts, envProxy);

		final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(Math.max(connectTimeoutMs, 1000));
		requestFactory.setReadTimeout(Math.max(readTimeoutMs, 1000));
		this.restTemplate = new RestTemplate(requestFactory);
	}

	public List<JobOffer> fetchTop10JobOffers(final String projectedKeywords) {
		final Map<String, JobOffer> uniqueOffers = new LinkedHashMap<>();
		final String safeKeywords = projectedKeywords == null ? "" : projectedKeywords;

		final List<String> keywords = Arrays.stream(safeKeywords.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());

		if (keywords.isEmpty()) {
			this.fetchOffers(null, uniqueOffers);
		} else
			for (final String keyword : keywords)
				this.fetchOffers(keyword, uniqueOffers);

		if (uniqueOffers.isEmpty()) {
			LOG.warn("No Jobicy offers found for keywords '{}'; trying a generic fetch.", safeKeywords);
			this.fetchOffers(null, uniqueOffers);
		}

		for (final JobOffer offer : uniqueOffers.values()) {
			int score = 0;
			final String titleLower = offer.getTitle() != null ? offer.getTitle().toLowerCase() : "";
			final String descLower = offer.getDescription() != null ? offer.getDescription().toLowerCase() : "";

			for (final String keyword : keywords) {
				final String kwLower = keyword.toLowerCase();
				if (titleLower.contains(kwLower))
					score += 2;
				if (descLower.contains(kwLower))
					score += 1;
			}
			offer.setScore(score);
		}

		return uniqueOffers.values().stream()
				.sorted(Comparator.comparingInt(JobOffer::getScore).reversed())
				.limit(10)
				.collect(Collectors.toList());
	}

	private void fetchOffers(final String keyword, final Map<String, JobOffer> uniqueOffers) {
		try {
			final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL).queryParam("count", 20);
			if (keyword != null && !keyword.isBlank())
				builder.queryParam("tag", keyword);

			final JobicyResponse response = this.restTemplate.getForObject(builder.build(true).toUri(), JobicyResponse.class);
			if (response == null || response.getJobs() == null)
				return;

			for (final JobicyJob job : response.getJobs()) {
				if (job == null)
					continue;

				final String url = this.normaliseUrl(job.getUrl());
				if (url == null)
					continue;

				final String key = this.keyFor(job);
				if (key == null || uniqueOffers.containsKey(key))
					continue;

				final JobOffer offer = new JobOffer();
				offer.setId(Math.abs(key.hashCode()));
				offer.setTitle(this.nullSafe(job.getJobTitle()));
				offer.setCompanyName(this.nullSafe(job.getCompanyName()));
				offer.setUrl(url);
				offer.setDescription(this.sanitizeDescription(job.getJobDescription(), job.getJobExcerpt()));
				uniqueOffers.put(key, offer);
			}

		} catch (final RestClientException e) {
			final String keywordLabel = keyword == null ? "<generic>" : keyword;
			LOG.warn("Jobicy query failed for keyword '{}': {}", keywordLabel, e.getMessage());
			this.logNetworkHintIfNeeded(e);
		}
	}

	private String keyFor(final JobicyJob job) {
		String result;

		if (job.getUrl() != null && !job.getUrl().isBlank())
			result = job.getUrl().trim();
		else if (job.getId() != null && !job.getId().isBlank())
			result = job.getId().trim();
		else
			result = null;

		return result;
	}

	private String sanitizeDescription(final String fullDescription, final String excerpt) {
		String result;
		String source;

		source = fullDescription != null && !fullDescription.isBlank() ? fullDescription : excerpt;
		if (source == null || source.isBlank())
			return "-";

		result = source.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
		if (result.length() > 220)
			result = result.substring(0, 220) + "...";

		return result;
	}

	private String nullSafe(final String value) {
		return value == null || value.isBlank() ? "-" : value.trim();
	}

	private String normaliseUrl(final String url) {
		String result;

		if (url == null || url.isBlank())
			result = null;
		else {
			result = url.trim();
			if (!result.startsWith("http://") && !result.startsWith("https://"))
				result = null;
		}

		return result;
	}

	private void configureNetworking(final boolean useSystemProxies, final boolean preferIpv4, final String proxyHost, final int proxyPort, final String proxyUser, final String proxyPassword, final String nonProxyHosts, final ProxySettings envProxy) {
		String effectiveProxyHost = proxyHost;
		int effectiveProxyPort = proxyPort;
		String effectiveProxyUser = proxyUser;
		String effectiveProxyPassword = proxyPassword;

		if ((effectiveProxyHost == null || effectiveProxyHost.isBlank()) && envProxy != null) {
			effectiveProxyHost = envProxy.host;
			effectiveProxyPort = envProxy.port;
			effectiveProxyUser = envProxy.user;
			effectiveProxyPassword = envProxy.password;
			LOG.info("Jobicy proxy loaded from environment variable HTTPS_PROXY.");
		}

		final boolean hasProxyHost = effectiveProxyHost != null && !effectiveProxyHost.isBlank();
		final boolean hasProxyUser = effectiveProxyUser != null && !effectiveProxyUser.isBlank();
		final boolean hasProxyPassword = effectiveProxyPassword != null && !effectiveProxyPassword.isBlank();
		final boolean hasNonProxyHosts = nonProxyHosts != null && !nonProxyHosts.isBlank();

		System.setProperty("java.net.useSystemProxies", Boolean.toString(useSystemProxies));
		if (preferIpv4)
			System.setProperty("java.net.preferIPv4Stack", "true");

		if (hasProxyHost) {
			final String host = effectiveProxyHost.trim();
			final String port = Integer.toString(effectiveProxyPort > 0 ? effectiveProxyPort : 8080);

			System.setProperty("https.proxyHost", host);
			System.setProperty("https.proxyPort", port);
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", port);

			if (hasNonProxyHosts)
				System.setProperty("http.nonProxyHosts", nonProxyHosts.trim());

			if (hasProxyUser && hasProxyPassword) {
				final String user = effectiveProxyUser.trim();
				final char[] password = effectiveProxyPassword.toCharArray();

				Authenticator.setDefault(new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, password);
					}
				});
			}

			LOG.info("Jobicy proxy enabled: {}:{} (system-proxies={}, ipv4={})", host, port, useSystemProxies, preferIpv4);
		} else
			LOG.info("Jobicy direct networking: system-proxies={}, ipv4={}", useSystemProxies, preferIpv4);
	}

	private ProxySettings extractProxyFromEnvironment() {
		ProxySettings result;
		String rawProxy;
		String normalised;
		URI uri;
		String userInfo;
		String user;
		String password;
		String[] credentials;

		rawProxy = System.getenv("HTTPS_PROXY");
		if (rawProxy == null || rawProxy.isBlank())
			rawProxy = System.getenv("https_proxy");

		if (rawProxy == null || rawProxy.isBlank())
			return null;

		try {
			normalised = rawProxy.contains("://") ? rawProxy.trim() : "http://" + rawProxy.trim();
			uri = URI.create(normalised);
			if (uri.getHost() == null)
				return null;

			userInfo = uri.getUserInfo();
			user = null;
			password = null;
			if (userInfo != null && !userInfo.isBlank()) {
				credentials = userInfo.split(":", 2);
				user = credentials[0];
				password = credentials.length > 1 ? credentials[1] : "";
			}

			result = new ProxySettings(uri.getHost(), uri.getPort() > 0 ? uri.getPort() : 8080, user, password);
		} catch (final RuntimeException oops) {
			result = null;
		}

		return result;
	}

	private void logNetworkHintIfNeeded(final RestClientException exception) {
		assert exception != null;

		final String message = exception.getMessage();
		final String lower = message == null ? "" : message.toLowerCase();
		final boolean timeoutOrConnectError = lower.contains("timed out") || lower.contains("connect");

		if (!this.networkHintLogged && timeoutOrConnectError && !this.explicitProxyConfigured) {
			this.networkHintLogged = true;
			LOG.warn("Jobicy network hint: if your environment requires a proxy, set jobicy.proxy.host and jobicy.proxy.port in application.properties.");
		}
	}

	private static final class ProxySettings {

		private final String	host;
		private final int		port;
		private final String	user;
		private final String	password;


		private ProxySettings(final String host, final int port, final String user, final String password) {
			this.host = host;
			this.port = port;
			this.user = user;
			this.password = password;
		}
	}
}
