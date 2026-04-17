
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
public class SpokespersonCampaignShowService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repo;
	private Campaign						entity;


	@Override
	public void load() {
		this.entity = this.repo.findCampaignById(super.getRequest().getData("id", int.class));
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.entity != null && (this.entity.getSpokesperson().isPrincipal() || !this.entity.getDraftMode()));
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
