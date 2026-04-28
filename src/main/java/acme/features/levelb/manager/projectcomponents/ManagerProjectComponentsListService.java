package acme.features.levelb.manager.projectcomponents;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectComponent;
import acme.features.levelb.project.ProjectComponentsSupport;
import acme.realms.Manager;

@Service
public class ManagerProjectComponentsListService extends AbstractService<Manager, ProjectComponent> {

	@Autowired
	private ProjectRepository repository;

	private Project project;

	private Collection<ProjectComponent> components;


	@Override
	public void load() {
		Integer projectId;

		projectId = super.getRequest().getData("projectId", Integer.class, null);
		if (projectId == null) {
			this.project = null;
			this.components = List.of();
			return;
		}

		this.project = this.repository.findProjectById(projectId);

		if (this.project == null)
			this.components = List.of();
		else
			this.components = ProjectComponentsSupport.assemble(this.repository.findInventionsByProjectId(projectId.intValue()), this.repository.findCampaignsByProjectId(projectId.intValue()), this.repository.findStrategiesByProjectId(projectId.intValue()));
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.project != null && this.project.getManager() != null && this.project.getManager().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("projectId", this.project == null ? 0 : this.project.getId());
		super.getResponse().addGlobal("projectTitle", this.project == null ? "-" : this.project.getTitle());
		super.unbindObjects(this.components, "id", "kindLabel", "ticker", "name", "owner", "draftMode", "startMoment", "endMoment");
	}

}
