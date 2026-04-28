package acme.features.levelb.inventor.projectComponents;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Inventor;
import acme.features.levelb.project.ProjectComponent;
import acme.features.levelb.project.ProjectComponentsSupport;

@Service
public class InventorProjectComponentsListService extends AbstractService<Inventor, ProjectComponent> {

	@Autowired
	private ProjectRepository repository;

	private Project project;

	private Collection<ProjectComponent> components;


	@Override
	public void load() {
		int projectId;

		projectId = super.getRequest().getData("projectId", int.class);
		this.project = this.repository.findProjectById(projectId);

		if (this.project == null)
			this.components = List.of();
		else
			this.components = ProjectComponentsSupport.assemble(this.repository.findInventionsByProjectId(projectId), this.repository.findCampaignsByProjectId(projectId), this.repository.findStrategiesByProjectId(projectId));
	}

	@Override
	public void authorise() {
		boolean status;
		int userAccountId;
		Long memberships;

		if (this.project == null)
			status = false;
		else {
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			memberships = this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), userAccountId, MemberRole.INVENTOR);
			status = memberships != null && memberships > 0;
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("projectId", this.project == null ? 0 : this.project.getId());
		super.getResponse().addGlobal("projectTitle", this.project == null ? "-" : this.project.getTitle());
		super.unbindObjects(this.components, "id", "kindLabel", "ticker", "name", "owner", "draftMode", "startMoment", "endMoment");
	}

}
