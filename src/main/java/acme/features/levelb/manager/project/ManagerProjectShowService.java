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
public class ManagerProjectShowService extends AbstractService<Manager, Project> {

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

		status = this.project != null && this.project.getId() != 0 && this.project.getManager() != null && this.project.getManager().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		tuple.put("id", this.project.getId() != 0 ? this.project.getId() : 0);
		ProjectSupport.putDetails(tuple, this.project, this.repository);
	}

}
