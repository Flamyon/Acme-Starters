
package acme.features.levelb.project;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import acme.client.components.models.Tuple;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectMember;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;

public final class ProjectSupport {

	private ProjectSupport() {
	}

	public static void putDetails(final Tuple tuple, final Project project) {
		assert tuple != null;
		assert project != null;

		tuple.put("membersSummary", ProjectSupport.formatMembers(project.getMembers()));
		tuple.put("inventionsSummary", ProjectSupport.formatInventions(project.getInventions()));
		tuple.put("campaignsSummary", ProjectSupport.formatCampaigns(project.getCampaigns()));
		tuple.put("strategiesSummary", ProjectSupport.formatStrategies(project.getStrategies()));
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

}
