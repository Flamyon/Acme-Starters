package acme.features.levelb.manager.projectmember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectMember;
import acme.entities.levelb.ProjectRepository;
import acme.realms.Manager;

@Service
public class ManagerProjectMemberListService extends AbstractService<Manager, ProjectMember> {

	@Autowired
	private ProjectRepository repository;

	private Collection<ProjectMember> members;

	private Project project;


	@Override
	public void load() {
		int projectId;

		projectId = super.getRequest().getData("projectId", int.class);
		this.project = this.repository.findProjectById(projectId);
		this.members = this.repository.findProjectMembersByProjectId(projectId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.project != null && this.project.getManager() != null && this.project.getManager().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean showCreate;

		showCreate = Boolean.TRUE.equals(this.project.getDraftMode()) && this.project.getManager().isPrincipal();
		super.getResponse().addGlobal("projectId", this.project.getId());
		super.getResponse().addGlobal("projectTitle", this.project.getTitle());
		super.getResponse().addGlobal("showCreate", showCreate);
		super.unbindObjects(this.members, "id", "memberFullName", "memberEmail", "roleLabel");
	}

}
