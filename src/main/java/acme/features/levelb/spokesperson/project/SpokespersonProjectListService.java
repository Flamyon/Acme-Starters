package acme.features.levelb.spokesperson.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonProjectListService extends AbstractService<Spokesperson, Project> {

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
		this.projects = this.repository.findProjectsByRoleMemberUserAccountId(userAccountId, MemberRole.SPOKESPERSON);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.projects, "title", "kickOff", "closeOut", "draftMode");
	}

}
