package acme.features.levelb.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.realms.Manager;

@Service
public class ManagerProjectListService extends AbstractService<Manager, Project> {

	@Autowired
	private ProjectRepository repository;

	private Collection<Project> projects;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.projects = this.repository.findProjectsByManagerId(managerId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.projects, "title", "kickOff", "closeOut", "draftMode");
	}

}
