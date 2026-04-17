package acme.features.student1.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Service
public class InventorInventionCreateService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository repo;
	private Invention entity;


	@Override
	public void load() {
		Inventor inventor;

		inventor = (Inventor) super.getRequest().getPrincipal().getActiveRealm();
		this.entity = this.newObject(Invention.class);
		this.entity.setInventor(inventor);
		this.entity.setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Inventor.class);
	}

	@Override
	public void bind() {
		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repo.findProjectById(projectId);
		this.entity.setProject(project);

		super.bindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		boolean validProject;

		super.validateObject(this.entity);

		validProject = this.entity.getProject() != null && this.repo.countDraftMembershipByProjectAndUserAccountId(this.entity.getProject().getId(), this.entity.getInventor().getUserAccount().getId()) > 0;
		super.state(validProject, "project", "inventor.invention.form.error.project");
	}

	@Override
	public void execute() {
		this.repo.save(this.entity);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices projects;
		Project selectedProject;

		selectedProject = Boolean.TRUE.equals(this.entity.getDraftMode()) ? this.entity.getProject() : null;
		projects = SelectChoices.from(this.repo.findAvailableProjectsByMemberUserAccountId(this.entity.getInventor().getUserAccount().getId()), "title", selectedProject);

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("cost", this.entity.getCost(this.entity.getId()));
		tuple.put("project", this.entity.getProject() == null ? 0 : this.entity.getProject().getId());
		tuple.put("projectTitle", this.entity.getProject() == null ? "-" : this.entity.getProject().getTitle());
		tuple.put("projects", projects);
	}
}
