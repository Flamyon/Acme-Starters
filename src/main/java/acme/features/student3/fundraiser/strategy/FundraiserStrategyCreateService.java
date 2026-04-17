
package acme.features.student3.fundraiser.strategy;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.levelb.Project;
import acme.entities.student3.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyCreateService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repo;
	private Strategy					entity;


	@Override
	public void load() {
		Fundraiser fundraiser;

		fundraiser = (Fundraiser) super.getRequest().getPrincipal().getActiveRealm();
		this.entity = this.newObject(Strategy.class);
		this.entity.setFundraiser(fundraiser);
		this.entity.setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Fundraiser.class);
	}

	@Override
	public void bind() {
		int projectId;
		Project project;

		projectId = super.getRequest().hasData("project", int.class) ? super.getRequest().getData("project", int.class) : 0;
		project = projectId == 0 ? null : this.repo.findProjectById(projectId);
		this.entity.setProject(project);

		super.bindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		boolean validProject;
		Long memberships;

		super.validateObject(this.entity);

		if (this.entity.getProject() == null)
			validProject = true;
		else {
			memberships = this.repo.countDraftMembershipByProjectAndUserAccountId(this.entity.getProject().getId(), this.entity.getFundraiser().getUserAccount().getId());
			validProject = memberships != null && memberships > 0;
		}
		super.state(validProject, "project", "fundraiser.strategy.form.error.project");

		// 1. ticker uniqueness (prevent DB unique-constraint errors on create)
		String ticker = this.entity.getTicker();
		if (ticker != null && !ticker.trim().isEmpty()) {
			Strategy other = this.repo.findStrategyByTicker(ticker);
			super.state(other == null, "ticker", "acme.validation.strategy.ticker-duplicated.message");
		}

		// 2. date checks: start and end must be present and start < end
		java.util.Date start = this.entity.getStartMoment();
		java.util.Date end = this.entity.getEndMoment();

		if (start == null) {
			super.state(false, "startMoment", "acme.validation.strategy.start-null.message");
		}

		if (end == null) {
			super.state(false, "endMoment", "acme.validation.strategy.end-null.message");
		}

		if (start != null && end != null) {
			super.state(MomentHelper.isBefore(start, end), "endMoment", "acme.validation.strategy.invalid-interval.message");
		}
	}

	@Override
	public void execute() {
		this.repo.save(this.entity);
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
