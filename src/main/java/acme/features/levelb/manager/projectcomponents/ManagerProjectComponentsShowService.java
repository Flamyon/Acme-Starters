package acme.features.levelb.manager.projectcomponents;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.features.levelb.project.ProjectComponent;
import acme.features.levelb.project.ProjectComponentsSupport;
import acme.realms.Manager;

@Service
public class ManagerProjectComponentsShowService extends AbstractService<Manager, ProjectComponent> {

	@Autowired
	private ProjectRepository repository;

	private ProjectComponent component;

	private Collection<ProjectComponent> candidates;


	@Override
	public void load() {
		Integer sourceId;

		sourceId = super.getRequest().getData("id", Integer.class, null);
		if (sourceId == null) {
			this.component = null;
			this.candidates = List.of();
			return;
		}

		this.component = null;
		this.candidates = ProjectComponentsSupport.resolveBySourceId(this.repository, sourceId.intValue());
	}

	@Override
	public void authorise() {
		boolean status;
		ProjectComponent authorised;

		status = false;
		authorised = null;

		for (ProjectComponent candidate : this.candidates) {
			Project project;
			boolean candidateAuthorised;

			project = this.repository.findProjectById(candidate.getProjectId());
			candidateAuthorised = project != null && project.getManager() != null && project.getManager().isPrincipal();

			if (candidateAuthorised)
				if (authorised == null)
					authorised = candidate;
				else {
					authorised = null;
					break;
				}
		}

		if (authorised != null) {
			this.component = authorised;
			status = true;
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.component, "kindLabel", "ticker", "name", "owner", "draftMode", "startMoment", "endMoment", "description", "moreInfo");
		tuple.put("projectId", this.component.getProjectId());
	}

}