
package acme.features.levelb.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;

public final class ProjectComponentsSupport {

	private static final int	CAMPAIGN_OFFSET	= 1_000_000_000;
	private static final int	STRATEGY_OFFSET	= -1_000_000_000;
	private static final int	THRESHOLD		= 500_000_000;


	private ProjectComponentsSupport() {
	}

	public static List<ProjectComponent> assemble(final Collection<Invention> inventions, final Collection<Campaign> campaigns, final Collection<Strategy> strategies) {
		List<ProjectComponent> result;

		result = new ArrayList<>();

		ProjectComponentsSupport.appendComponents(result, inventions, ProjectComponentsSupport::fromInvention);
		ProjectComponentsSupport.appendComponents(result, campaigns, ProjectComponentsSupport::fromCampaign);
		ProjectComponentsSupport.appendComponents(result, strategies, ProjectComponentsSupport::fromStrategy);

		result.sort(Comparator.comparing(ProjectComponent::getStartMoment, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(ProjectComponent::getKindLabel).thenComparing(ProjectComponent::getTicker,
			Comparator.nullsLast(String::compareToIgnoreCase)));

		return result;
	}

	private static <T> void appendComponents(final List<ProjectComponent> destination, final Collection<T> source, final Function<T, ProjectComponent> mapper) {
		assert destination != null;
		assert mapper != null;

		if (source == null)
			return;

		for (T element : source)
			if (element != null)
				destination.add(mapper.apply(element));
	}

	public static ProjectComponent fromInvention(final Invention invention) {
		assert invention != null;

		ProjectComponent result;

		result = new ProjectComponent();
		result.setId(invention.getId());
		result.setKind(ProjectComponent.Kind.INVENTION);
		result.setSourceId(invention.getId());
		result.setProjectId(invention.getProject() == null ? 0 : invention.getProject().getId());
		result.setTicker(invention.getTicker());
		result.setName(invention.getName());
		result.setDescription(invention.getDescription());
		result.setStartMoment(invention.getStartMoment());
		result.setEndMoment(invention.getEndMoment());
		result.setMoreInfo(invention.getMoreInfo());
		result.setDraftMode(invention.getDraftMode());
		result.setOwner(invention.getInventor() != null && invention.getInventor().getIdentity() != null ? invention.getInventor().getIdentity().getFullName() : "-");

		return result;
	}

	public static ProjectComponent fromCampaign(final Campaign campaign) {
		assert campaign != null;

		ProjectComponent result;

		result = new ProjectComponent();
		result.setId(ProjectComponentsSupport.CAMPAIGN_OFFSET + campaign.getId());
		result.setKind(ProjectComponent.Kind.CAMPAIGN);
		result.setSourceId(campaign.getId());
		result.setProjectId(campaign.getProject() == null ? 0 : campaign.getProject().getId());
		result.setTicker(campaign.getTicker());
		result.setName(campaign.getName());
		result.setDescription(campaign.getDescription());
		result.setStartMoment(campaign.getStartMoment());
		result.setEndMoment(campaign.getEndMoment());
		result.setMoreInfo(campaign.getMoreInfo());
		result.setDraftMode(campaign.getDraftMode());
		result.setOwner(campaign.getSpokesperson() != null && campaign.getSpokesperson().getIdentity() != null ? campaign.getSpokesperson().getIdentity().getFullName() : "-");

		return result;
	}

	public static ProjectComponent fromStrategy(final Strategy strategy) {
		assert strategy != null;

		ProjectComponent result;

		result = new ProjectComponent();
		result.setId(ProjectComponentsSupport.STRATEGY_OFFSET + strategy.getId());
		result.setKind(ProjectComponent.Kind.STRATEGY);
		result.setSourceId(strategy.getId());
		result.setProjectId(strategy.getProject() == null ? 0 : strategy.getProject().getId());
		result.setTicker(strategy.getTicker());
		result.setName(strategy.getName());
		result.setDescription(strategy.getDescription());
		result.setStartMoment(strategy.getStartMoment());
		result.setEndMoment(strategy.getEndMoment());
		result.setMoreInfo(strategy.getMoreInfo());
		result.setDraftMode(strategy.getDraftMode());
		result.setOwner(strategy.getFundraiser() != null && strategy.getFundraiser().getIdentity() != null ? strategy.getFundraiser().getIdentity().getFullName() : "-");

		return result;
	}

	public static List<ProjectComponent> resolveBySourceId(final ProjectRepository repository, final int sourceId) {
		assert repository != null;

		List<ProjectComponent> result;
		ProjectComponent.Kind kind;
		int decodedSourceId;
		Invention invention;
		Campaign campaign;
		Strategy strategy;

		result = new ArrayList<>();
		kind = ProjectComponentsSupport.decodeKind(sourceId);
		decodedSourceId = ProjectComponentsSupport.decodeSourceId(sourceId);

		switch (kind) {
		case INVENTION:
			invention = repository.findInventionById(decodedSourceId);
			if (invention != null)
				result.add(ProjectComponentsSupport.fromInvention(invention));
			break;
		case CAMPAIGN:
			campaign = repository.findCampaignById(decodedSourceId);
			if (campaign != null)
				result.add(ProjectComponentsSupport.fromCampaign(campaign));
			break;
		case STRATEGY:
			strategy = repository.findStrategyById(decodedSourceId);
			if (strategy != null)
				result.add(ProjectComponentsSupport.fromStrategy(strategy));
			break;
		default:
			break;
		}

		return result;
	}

	public static ProjectComponent.Kind decodeKind(final int encodedId) {
		ProjectComponent.Kind result;

		if (encodedId > ProjectComponentsSupport.THRESHOLD)
			result = ProjectComponent.Kind.CAMPAIGN;
		else if (encodedId < -ProjectComponentsSupport.THRESHOLD)
			result = ProjectComponent.Kind.STRATEGY;
		else
			result = ProjectComponent.Kind.INVENTION;

		return result;
	}

	public static int decodeSourceId(final int encodedId) {
		int result;
		ProjectComponent.Kind kind;

		kind = ProjectComponentsSupport.decodeKind(encodedId);
		switch (kind) {
		case CAMPAIGN:
			result = encodedId - ProjectComponentsSupport.CAMPAIGN_OFFSET;
			break;
		case STRATEGY:
			result = encodedId - ProjectComponentsSupport.STRATEGY_OFFSET;
			break;
		case INVENTION:
		default:
			result = encodedId;
			break;
		}

		return result;
	}

}
