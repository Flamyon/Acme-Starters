package acme.features.levelb.fundraiser.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectSupport;
import acme.realms.Fundraiser;

@Service
public class FundraiserProjectShowService extends AbstractService<Fundraiser, Project> {

	@Autowired
	private ProjectRepository repository;

	private Project project;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.project = this.repository.findProjectByIdWithDetails(id);
	}

	@Override
	public void authorise() {
		boolean status;
		boolean isMember;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		isMember = this.project != null && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), userAccountId, MemberRole.FUNDRAISER) > 0;
		status = this.project != null && (Boolean.FALSE.equals(this.project.getDraftMode()) || isMember);

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		int userAccountId;
		boolean isMember;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode", "manager.identity.fullName");
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		isMember = this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), userAccountId, MemberRole.FUNDRAISER) > 0;
		tuple.put("showComponents", isMember);
		ProjectSupport.putDetails(tuple, this.project);
	}

}
