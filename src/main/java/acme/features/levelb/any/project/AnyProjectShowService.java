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

		status = this.project != null && this.project.getId() != 0 && Boolean.FALSE.equals(this.project.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		String managerFullName;
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		tuple.put("id", this.project.getId() != 0 ? this.project.getId() : 0);
		managerFullName = this.project.getManager() != null && this.project.getManager().getIdentity() != null && this.project.getManager().getIdentity().getFullName() != null ? this.project.getManager().getIdentity().getFullName() : "-";
		tuple.put("manager.identity.fullName", managerFullName);
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
