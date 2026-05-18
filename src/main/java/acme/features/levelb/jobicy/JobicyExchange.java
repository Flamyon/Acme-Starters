
package acme.features.levelb.jobicy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import acme.forms.JobOffer;

@Component
public class JobicyExchange {

	private static final Logger	LOG		= LoggerFactory.getLogger(JobicyExchange.class);

	private static final String	API_URL	= "https://jobicy.com/api/v2/remote-jobs";

	private final RestTemplate	restTemplate;


	public JobicyExchange( //
		@Value("${jobicy.http.connect-timeout-ms:10000}") final int connectTimeoutMs, //
		@Value("${jobicy.http.read-timeout-ms:10000}") final int readTimeoutMs //
	) {
		final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(Math.max(connectTimeoutMs, 1000));
		requestFactory.setReadTimeout(Math.max(readTimeoutMs, 1000));
		this.restTemplate = new RestTemplate(requestFactory);
	}

	public List<JobOffer> fetchTop10JobOffers(final String projectedKeywords) {
		final Map<String, JobOffer> uniqueOffers = new LinkedHashMap<>();
		final List<String> keywords = Arrays.stream((projectedKeywords == null ? "" : projectedKeywords).split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();

		keywords.forEach(keyword -> this.fetchOffers(keyword, uniqueOffers));

		if (uniqueOffers.isEmpty()) {
			JobicyExchange.LOG.warn("No Jobicy offers found for keywords '{}'; trying a generic fetch.", projectedKeywords);
			this.fetchOffers(null, uniqueOffers);
		}

		uniqueOffers.values().forEach(offer -> {
			int score = this.calculateScore(offer, keywords);
			offer.setScore(score);
		});

		return uniqueOffers.values().stream().sorted(Comparator.comparingInt(JobOffer::getScore).reversed()).limit(10).toList();
	}

	private int calculateScore(final JobOffer offer, final List<String> keywords) {
		final String titleLower = offer.getTitle() != null ? offer.getTitle().toLowerCase() : "";
		final String descLower = offer.getDescription() != null ? offer.getDescription().toLowerCase() : "";

		return keywords.stream().mapToInt(keyword -> {
			final String kwLower = keyword.toLowerCase();
			return (titleLower.contains(kwLower) ? 2 : 0) + (descLower.contains(kwLower) ? 1 : 0);
		}).sum();
	}

	private void fetchOffers(final String keyword, final Map<String, JobOffer> uniqueOffers) {
		try {
			final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(JobicyExchange.API_URL).queryParam("count", 20);
			if (keyword != null && !keyword.isBlank())
				builder.queryParam("tag", keyword);

			final JobicyResponse response = this.restTemplate.getForObject(builder.build(true).toUri(), JobicyResponse.class);
			if (response == null || response.getJobs() == null)
				return;

			response.getJobs().stream().filter(job -> job != null && this.normaliseUrl(job.getUrl()) != null).forEach(job -> {
				final String key = this.keyFor(job);
				if (key != null && !uniqueOffers.containsKey(key))
					uniqueOffers.put(key, this.createJobOffer(job, key));
			});
		} catch (final RestClientException e) {
			JobicyExchange.LOG.warn("Jobicy query failed for keyword '{}': {}", keyword == null ? "<generic>" : keyword, e.getMessage());
		}
	}

	private JobOffer createJobOffer(final JobicyJob job, final String key) {
		final JobOffer offer = new JobOffer();
		offer.setId(Math.abs(key.hashCode()));
		offer.setTitle(this.nullSafe(job.getJobTitle()));
		offer.setCompanyName(this.nullSafe(job.getCompanyName()));
		offer.setUrl(this.normaliseUrl(job.getUrl()));
		offer.setDescription(this.sanitizeDescription(job.getJobDescription(), job.getJobExcerpt()));
		return offer;
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
}
