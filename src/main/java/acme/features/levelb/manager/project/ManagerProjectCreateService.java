package acme.features.levelb.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectSupport;
import acme.realms.Manager;

@Service
public class ManagerProjectCreateService extends AbstractService<Manager, Project> {

	@Autowired
	private ProjectRepository repository;

	private Project project;


	@Override
	public void load() {
		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		this.project = super.newObject(Project.class);
		this.project.setDraftMode(true);
		this.project.setManager(manager);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Manager.class);
	}

	@Override
	public void bind() {
		super.bindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut");
	}

	@Override
	public void validate() {
		super.validateObject(this.project);
	}

	@Override
	public void execute() {
		this.repository.save(this.project);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		tuple.put("id", this.project.getId() != 0 ? this.project.getId() : 0);
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
