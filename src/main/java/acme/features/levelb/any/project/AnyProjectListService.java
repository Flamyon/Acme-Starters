package acme.features.levelb.any.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;

@Service
public class AnyProjectListService extends AbstractService<Any, Project> {

	@Autowired
	private ProjectRepository repository;

	private Collection<Project> projects;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		this.projects = this.repository.findPublishedProjects();
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.projects, "title", "kickOff", "closeOut", "manager.identity.fullName");
	}

}
