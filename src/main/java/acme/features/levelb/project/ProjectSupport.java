
package acme.features.levelb.project;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import acme.client.components.models.Tuple;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.levelb.ProjectMember;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;
import acme.entities.student4.Sponsorship;
import acme.entities.student5.AuditReport;

public final class ProjectSupport {

	private ProjectSupport() {
	}

	public static void putDetails(final Tuple tuple, final Project project, final ProjectRepository repository) {
		Collection<ProjectMember> members;
		Collection<Invention> inventions;
		Collection<Campaign> campaigns;
		Collection<Strategy> strategies;
		Collection<Sponsorship> sponsorships;
		Collection<AuditReport> auditReports;

		assert tuple != null;
		assert project != null;
		assert repository != null;

		if (project.getId() == 0) {
			members = Collections.emptyList();
			inventions = Collections.emptyList();
			campaigns = Collections.emptyList();
			strategies = Collections.emptyList();
			sponsorships = Collections.emptyList();
			auditReports = Collections.emptyList();
		} else {
			members = repository.findProjectMembersByProjectId(project.getId());
			inventions = repository.findInventionsByProjectId(project.getId());
			campaigns = repository.findCampaignsByProjectId(project.getId());
			strategies = repository.findStrategiesByProjectId(project.getId());
			sponsorships = repository.findSponsorshipsByProjectId(project.getId());
			auditReports = repository.findAuditReportsByProjectId(project.getId());
		}

		tuple.put("membersSummary", ProjectSupport.formatMembers(members));
		tuple.put("inventionsSummary", ProjectSupport.formatInventions(inventions));
		tuple.put("campaignsSummary", ProjectSupport.formatCampaigns(campaigns));
		tuple.put("strategiesSummary", ProjectSupport.formatStrategies(strategies));
		tuple.put("sponsorshipsSummary", ProjectSupport.formatSponsorships(sponsorships));
		tuple.put("auditReportsSummary", ProjectSupport.formatAuditReports(auditReports));
	}

	private static String formatMembers(final Collection<ProjectMember> members) {
		if (members == null || members.isEmpty())
			return "-";

		String result;

		result = members.stream()
			.map(pm -> pm != null ? String.format("%s (%s)", pm.getMemberFullName(), pm.getRoleLabel()) : null)
			.filter(Objects::nonNull)
			.sorted()
			.collect(Collectors.joining(", "));

		return result.isEmpty() ? "-" : result;
	}

	private static String formatInventions(final Collection<Invention> inventions) {
		if (inventions == null || inventions.isEmpty())
			return "-";

		String result;

		result = inventions.stream()
			.filter(Objects::nonNull)
			.sorted(Comparator.comparing(Invention::getTicker, Comparator.nullsLast(String::compareToIgnoreCase)))
			.map(i -> i.getTicker() + " - " + i.getName())
			.collect(Collectors.joining(", "));

		return result.isEmpty() ? "-" : result;
	}

	private static String formatCampaigns(final Collection<Campaign> campaigns) {
		if (campaigns == null || campaigns.isEmpty())
			return "-";

		String result;

		result = campaigns.stream()
			.filter(Objects::nonNull)
			.sorted(Comparator.comparing(Campaign::getTicker, Comparator.nullsLast(String::compareToIgnoreCase)))
			.map(c -> c.getTicker() + " - " + c.getName())
			.collect(Collectors.joining(", "));

		return result.isEmpty() ? "-" : result;
	}

	private static String formatStrategies(final Collection<Strategy> strategies) {
		if (strategies == null || strategies.isEmpty())
			return "-";

		String result;

		result = strategies.stream()
			.filter(Objects::nonNull)
			.sorted(Comparator.comparing(Strategy::getTicker, Comparator.nullsLast(String::compareToIgnoreCase)))
			.map(s -> s.getTicker() + " - " + s.getName())
			.collect(Collectors.joining(", "));

		return result.isEmpty() ? "-" : result;
	}

	private static String formatSponsorships(final Collection<Sponsorship> sponsorships) {
		if (sponsorships == null || sponsorships.isEmpty())
			return "-";

		String result;

		result = sponsorships.stream()
			.filter(s -> s != null && Boolean.FALSE.equals(s.getDraftMode()))
			.sorted(Comparator.comparing(Sponsorship::getTicker, Comparator.nullsLast(String::compareToIgnoreCase)))
			.map(s -> s.getTicker() + " - " + s.getName())
			.collect(Collectors.joining(", "));

		return result.isEmpty() ? "-" : result;
	}

	private static String formatAuditReports(final Collection<AuditReport> auditReports) {
		if (auditReports == null || auditReports.isEmpty())
			return "-";

		String result;

		result = auditReports.stream()
			.filter(r -> r != null && Boolean.FALSE.equals(r.getDraftMode()))
			.sorted(Comparator.comparing(AuditReport::getTicker, Comparator.nullsLast(String::compareToIgnoreCase)))
			.map(r -> r.getTicker() + " - " + r.getName())
			.collect(Collectors.joining(", "));

		return result.isEmpty() ? "-" : result;
	}

}
