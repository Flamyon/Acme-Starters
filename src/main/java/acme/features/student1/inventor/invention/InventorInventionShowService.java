package acme.features.student1.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.entities.levelb.Project;
import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Service
public class InventorInventionShowService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository repo;

	private Invention entity;

	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity = this.repo.findInventionById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entity != null && (this.entity.getInventor().isPrincipal() || !this.entity.getDraftMode());
		super.setAuthorised(status);
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
