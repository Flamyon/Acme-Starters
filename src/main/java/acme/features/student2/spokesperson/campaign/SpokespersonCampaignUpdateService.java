
package acme.features.student2.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.student2.Campaign;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonCampaignUpdateService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repo;
	private Campaign						entity;


	@Override
	public void load() {
		this.entity = this.repo.findCampaignById(super.getRequest().getData("id", int.class));
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.entity != null && Boolean.TRUE.equals(this.entity.getDraftMode()) && this.entity.getSpokesperson() != null && this.entity.getSpokesperson().isPrincipal());
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

		validProject = this.entity.getProject() != null && this.repo.countDraftMembershipByProjectAndUserAccountId(this.entity.getProject().getId(), this.entity.getSpokesperson().getUserAccount().getId()) > 0;
		super.state(validProject, "project", "spokesperson.campaign.form.error.project");
	}

	@Override
	public void execute() {
		this.repo.save(this.entity);
	}
	@Override
	public void unbind() {
		Tuple t;
		SelectChoices projects;
		Project selectedProject;

		selectedProject = Boolean.TRUE.equals(this.entity.getDraftMode()) ? this.entity.getProject() : null;
		projects = SelectChoices.from(this.repo.findAvailableProjectsByMemberUserAccountId(this.entity.getSpokesperson().getUserAccount().getId()), "title", selectedProject);

		t = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		t.put("monthsActive", this.entity.getMonthsActive());
		t.put("effort", this.entity.getEffort());
		t.put("project", this.entity.getProject() == null ? 0 : this.entity.getProject().getId());
		t.put("projectTitle", this.entity.getProject() == null ? "-" : this.entity.getProject().getTitle());
		t.put("projects", projects);
	}
}
