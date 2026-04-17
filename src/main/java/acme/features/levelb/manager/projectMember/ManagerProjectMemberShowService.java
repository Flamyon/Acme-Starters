package acme.features.levelb.manager.projectMember;

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

		id = super.getRequest().getData("id", Integer.class, null);
		if (id == null)
			this.projectMember = null;
		else
			this.projectMember = this.repository.findProjectMemberById(id.intValue());
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.projectMember != null && this.projectMember.getProject() != null && this.projectMember.getProject().getManager() != null && this.projectMember.getProject().getManager().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.projectMember, "memberFullName", "memberEmail", "roleLabel");
		tuple.put("projectId", this.projectMember.getProject().getId());
		tuple.put("draftMode", this.projectMember.getProject().getDraftMode());
	}

}
