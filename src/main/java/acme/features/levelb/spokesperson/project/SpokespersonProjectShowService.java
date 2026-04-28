package acme.features.levelb.spokesperson.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student2.Spokesperson;
import acme.features.levelb.project.ProjectSupport;

@Service
public class SpokespersonProjectShowService extends AbstractService<Spokesperson, Project> {

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
		boolean isMember;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		isMember = this.project != null && this.project.getId() != 0 && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), userAccountId, MemberRole.SPOKESPERSON) > 0;
		status = this.project != null && this.project.getId() != 0 && (Boolean.FALSE.equals(this.project.getDraftMode()) || isMember);

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		String managerFullName;
		Tuple tuple;
		int userAccountId;
		boolean isMember;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		tuple.put("id", this.project.getId() != 0 ? this.project.getId() : 0);
		managerFullName = this.project.getManager() != null && this.project.getManager().getIdentity() != null && this.project.getManager().getIdentity().getFullName() != null ? this.project.getManager().getIdentity().getFullName() : "-";
		tuple.put("manager.identity.fullName", managerFullName);
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		isMember = this.project.getId() != 0 && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), userAccountId, MemberRole.SPOKESPERSON) > 0;
		tuple.put("showComponents", isMember);
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
