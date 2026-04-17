package acme.features.levelb.inventor.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.levelb.MemberRole;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Inventor;

@Service
public class InventorProjectListService extends AbstractService<Inventor, Project> {

	@Autowired
	private ProjectRepository repository;

	private Collection<Project> projects;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		this.projects = this.repository.findProjectsByRoleMemberUserAccountId(userAccountId, MemberRole.INVENTOR);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.projects, "title", "kickOff", "closeOut", "draftMode");
	}

}
