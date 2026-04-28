package acme.features.levelb.manager.projectmember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.ProjectMember;
import acme.entities.levelb.ProjectRepository;
import acme.realms.Manager;

@Service
public class ManagerProjectMemberShowService extends AbstractService<Manager, ProjectMember> {

	@Autowired
	private ProjectRepository repository;

	private ProjectMember projectMember;


	@Override
	public void load() {
		Integer id;

		this.projectMember = super.newObject(ProjectMember.class);
		id = super.getRequest().getData("id", Integer.class, null);
		if (id == null || id.intValue() == 0)
			return;
		this.projectMember = this.repository.findProjectMemberById(id.intValue());
		if (this.projectMember == null)
			this.projectMember = super.newObject(ProjectMember.class);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.projectMember != null && this.projectMember.getId() != 0 && this.projectMember.getProject() != null && this.projectMember.getProject().getManager() != null && this.projectMember.getProject().getManager().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.projectMember, "memberFullName", "memberEmail", "roleLabel");
		tuple.put("projectId", this.projectMember.getProject() == null ? 0 : this.projectMember.getProject().getId());
		tuple.put("draftMode", this.projectMember.getProject() == null ? false : this.projectMember.getProject().getDraftMode());
	}

}
