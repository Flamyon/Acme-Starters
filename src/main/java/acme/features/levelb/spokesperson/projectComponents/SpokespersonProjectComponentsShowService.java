package acme.features.levelb.spokesperson.projectComponents;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student2.Spokesperson;
import acme.features.levelb.project.ProjectComponent;
import acme.features.levelb.project.ProjectComponentsSupport;

@Service
public class SpokespersonProjectComponentsShowService extends AbstractService<Spokesperson, ProjectComponent> {

	@Autowired
	private ProjectRepository repository;

	private ProjectComponent component;

	private Collection<ProjectComponent> candidates;


	@Override
	public void load() {
		Integer sourceId;

		sourceId = super.getRequest().getData("id", Integer.class, null);
		if (sourceId == null) {
			this.component = null;
			this.candidates = List.of();
			return;
		}

		this.component = null;
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

			memberships = this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(candidate.getProjectId(), userAccountId, MemberRole.SPOKESPERSON);
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