package acme.features.levelb.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.entities.levelb.ProjectMember;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectSupport;
import acme.realms.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	@Autowired
	private ProjectRepository repository;

	private Project project;


	@Override
	public void load() {
		Integer id;

		id = super.getRequest().getData("id", Integer.class, null);
		if (id == null || id.intValue() == 0)
			this.project = super.newObject(Project.class);
		else {
			this.project = this.repository.findProjectByIdWithDetails(id.intValue());
			if (this.project == null)
				this.project = super.newObject(Project.class);
		}
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.project != null && this.project.getId() != 0 && this.project.getManager() != null && this.project.getManager().isPrincipal() && Boolean.TRUE.equals(this.project.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut");
	}

	@Override
	public void validate() {
		Long components;

		if (this.project.getId() == 0)
			return;

		components = this.repository.countInventionsByProjectId(this.project.getId()) + this.repository.countCampaignsByProjectId(this.project.getId()) + this.repository.countStrategiesByProjectId(this.project.getId());
		super.state(components == 0L, "draftMode", "acme.validation.project.has-components.message");
	}

	@Override
	public void execute() {
		Collection<ProjectMember> members;

		if (this.project.getId() == 0)
			return;

		members = this.repository.findProjectMembersByProjectId(this.project.getId());
		this.repository.deleteAll(members);
		this.repository.delete(this.project);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		tuple.put("id", this.project.getId() != 0 ? this.project.getId() : 0);
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
