package acme.features.levelb.any.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectSupport;

@Service
public class AnyProjectShowService extends AbstractService<Any, Project> {

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

		status = this.project != null && Boolean.FALSE.equals(this.project.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode", "manager.identity.fullName");
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
