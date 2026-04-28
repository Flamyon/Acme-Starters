package acme.features.levelb.fundraiser.projectComponents;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectComponent;
import acme.features.levelb.project.ProjectComponentsSupport;
import acme.realms.Fundraiser;

@Service
public class FundraiserProjectComponentsShowService extends AbstractService<Fundraiser, ProjectComponent> {

	@Autowired
	private ProjectRepository repository;

	private ProjectComponent component;

	private Collection<ProjectComponent> candidates;


	@Override
	public void load() {
		Integer sourceId;

		this.component = super.newObject(ProjectComponent.class);
		this.candidates = List.of();
		sourceId = super.getRequest().getData("id", Integer.class, null);
		if (sourceId == null || sourceId.intValue() == 0)
			return;
		this.candidates = ProjectComponentsSupport.resolveBySourceId(this.repository, sourceId.intValue());
	}

	@Override
	public void authorise() {
		boolean status;
		int userAccountId;
		ProjectComponent authorised;

		status = false;
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		authorised = null;

		for (ProjectComponent candidate : this.candidates) {
			Long memberships;
			boolean candidateAuthorised;

			memberships = this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(candidate.getProjectId(), userAccountId, MemberRole.FUNDRAISER);
			candidateAuthorised = memberships != null && memberships > 0;

			if (candidateAuthorised)
				if (authorised == null)
					authorised = candidate;
				else {
					authorised = null;
					break;
				}
		}

		if (authorised != null) {
			this.component = authorised;
			status = true;
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
