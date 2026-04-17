
package acme.features.student3.fundraiser.strategy;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.student3.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyShowService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repo;

	private Strategy					entity;

	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity = this.repo.findStrategyById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;
		Fundraiser fundraiser;

		fundraiser = (Fundraiser) super.getRequest().getPrincipal().getActiveRealm();
		status = this.entity != null && this.entity.getFundraiser() != null && this.entity.getFundraiser().getId() == fundraiser.getId();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices projects;
		Collection<Project> availableProjects;
		Project selectedProject;

		availableProjects = this.repo.findAvailableProjectsByMemberUserAccountId(this.entity.getFundraiser().getUserAccount().getId());
		selectedProject = Boolean.TRUE.equals(this.entity.getDraftMode()) && this.entity.getProject() != null && availableProjects.stream().anyMatch(p -> p.getId() == this.entity.getProject().getId()) ? this.entity.getProject() : null;
		projects = SelectChoices.from(availableProjects, "title", selectedProject);

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("expectedPercentage", this.entity.getExpectedPercentage());
		tuple.put("project", this.entity.getProject() == null ? 0 : this.entity.getProject().getId());
		tuple.put("projectTitle", this.entity.getProject() == null ? "-" : this.entity.getProject().getTitle());
		tuple.put("projects", projects);
	}

}
