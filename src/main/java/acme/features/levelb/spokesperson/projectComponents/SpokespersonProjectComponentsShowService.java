package acme.features.levelb.spokesperson.projectComponents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student2.Spokesperson;
import acme.entities.student3.Strategy;
import acme.features.levelb.project.ProjectComponent;
import acme.features.levelb.project.ProjectComponentsSupport;

@Service
public class SpokespersonProjectComponentsShowService extends AbstractService<Spokesperson, ProjectComponent> {

	@Autowired
	private ProjectRepository repository;

	private ProjectComponent component;


	@Override
	public void load() {
		Integer encodedId;
		int sourceId;
		ProjectComponent.Kind kind;

		encodedId = super.getRequest().getData("id", Integer.class, null);
		if (encodedId == null) {
			this.component = null;
			return;
		}

		sourceId = ProjectComponentsSupport.decodeSourceId(encodedId);
		kind = ProjectComponentsSupport.decodeKind(encodedId);

		switch (kind) {
		case INVENTION:
			Invention invention;

			invention = this.repository.findInventionById(sourceId);
			this.component = invention == null ? null : ProjectComponentsSupport.fromInvention(invention);
			break;
		case CAMPAIGN:
			Campaign campaign;

			campaign = this.repository.findCampaignById(sourceId);
			this.component = campaign == null ? null : ProjectComponentsSupport.fromCampaign(campaign);
			break;
		case STRATEGY:
			Strategy strategy;

			strategy = this.repository.findStrategyById(sourceId);
			this.component = strategy == null ? null : ProjectComponentsSupport.fromStrategy(strategy);
			break;
		default:
			this.component = null;
			break;
		}
	}

	@Override
	public void authorise() {
		boolean status;
		Long memberships;
		int userAccountId;

		if (this.component == null)
			status = false;
		else {
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			memberships = this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.component.getProjectId(), userAccountId, MemberRole.SPOKESPERSON);
			status = memberships != null && memberships > 0;
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.component, "kindLabel", "ticker", "name", "owner", "draftMode", "startMoment", "endMoment", "description", "moreInfo");
		tuple.put("projectId", this.component.getProjectId());
	}

}