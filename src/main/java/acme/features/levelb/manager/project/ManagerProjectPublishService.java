package acme.features.levelb.manager.project;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.levelb.MemberRole;
import acme.entities.levelb.Project;
import acme.entities.levelb.ProjectRepository;
import acme.entities.student1.Invention;
import acme.entities.student2.Campaign;
import acme.entities.student3.Strategy;
import acme.features.levelb.project.ProjectSupport;
import acme.realms.Manager;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

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

		status = this.project != null && this.project.getManager() != null && this.project.getManager().isPrincipal() && Boolean.TRUE.equals(this.project.getDraftMode());
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
	}

	@Override
	public void validate() {
		boolean hasInventions;
		boolean allComponentsPublishable;

		super.validateObject(this.project);

		hasInventions = this.repository.countInventionsByProjectId(this.project.getId()) > 0;
		super.state(hasInventions, "*", "acme.validation.project.no-inventions.message");

		allComponentsPublishable = this.validateInventions() && this.validateCampaigns() && this.validateStrategies();
		super.state(allComponentsPublishable, "*", "acme.validation.project.unpublishable-components.message");
	}

	@Override
	@Transactional
	public void execute() {
		this.project.getInventions().forEach(i -> i.setDraftMode(false));
		this.project.getCampaigns().forEach(c -> c.setDraftMode(false));
		this.project.getStrategies().forEach(s -> s.setDraftMode(false));
		this.project.setDraftMode(false);

		this.repository.saveAll(this.project.getInventions());
		this.repository.saveAll(this.project.getCampaigns());
		this.repository.saveAll(this.project.getStrategies());
		this.repository.save(this.project);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.project, "title", "keywords", "description", "kickOff", "closeOut", "draftMode");
		ProjectSupport.putDetails(tuple, this.project);
	}

	private boolean validateInventions() {
		boolean isValid;
		Date now;

		now = MomentHelper.getCurrentMoment();
		isValid = true;

		for (Invention invention : this.project.getInventions()) {
			boolean ownerIsMember;
			boolean publishable;
			Long parts;
			Integer ownerUserAccountId;

			ownerUserAccountId = invention != null && invention.getInventor() != null && invention.getInventor().getUserAccount() != null ? invention.getInventor().getUserAccount().getId() : null;
			ownerIsMember = ownerUserAccountId != null && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), ownerUserAccountId, MemberRole.INVENTOR) > 0;
			super.state(ownerIsMember, "*", "acme.validation.project.component-owner-not-member.message");
			isValid &= ownerIsMember;

			if (Boolean.FALSE.equals(invention.getDraftMode()))
				continue;

			parts = this.repository.countPartsByInventionId(invention.getId());
			publishable = parts != null && parts > 0 && this.isFutureInterval(invention.getStartMoment(), invention.getEndMoment(), now);
			isValid &= publishable;
		}

		return isValid;
	}

	private boolean validateCampaigns() {
		boolean isValid;
		Date now;

		now = MomentHelper.getCurrentMoment();
		isValid = true;

		for (Campaign campaign : this.project.getCampaigns()) {
			boolean ownerIsMember;
			boolean publishable;
			Long milestones;
			Integer ownerUserAccountId;

			ownerUserAccountId = campaign != null && campaign.getSpokesperson() != null && campaign.getSpokesperson().getUserAccount() != null ? campaign.getSpokesperson().getUserAccount().getId() : null;
			ownerIsMember = ownerUserAccountId != null && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), ownerUserAccountId, MemberRole.SPOKESPERSON) > 0;
			super.state(ownerIsMember, "*", "acme.validation.project.component-owner-not-member.message");
			isValid &= ownerIsMember;

			if (Boolean.FALSE.equals(campaign.getDraftMode()))
				continue;

			milestones = this.repository.countMilestonesByCampaignId(campaign.getId());
			publishable = milestones != null && milestones > 0 && this.isFutureInterval(campaign.getStartMoment(), campaign.getEndMoment(), now);
			isValid &= publishable;
		}

		return isValid;
	}

	private boolean validateStrategies() {
		boolean isValid;
		Date now;

		now = MomentHelper.getCurrentMoment();
		isValid = true;

		for (Strategy strategy : this.project.getStrategies()) {
			boolean ownerIsMember;
			boolean publishable;
			Long tactics;
			Integer ownerUserAccountId;

			ownerUserAccountId = strategy != null && strategy.getFundraiser() != null && strategy.getFundraiser().getUserAccount() != null ? strategy.getFundraiser().getUserAccount().getId() : null;
			ownerIsMember = ownerUserAccountId != null && this.repository.countProjectMemberByProjectIdAndRoleKindAndUserAccountId(this.project.getId(), ownerUserAccountId, MemberRole.FUNDRAISER) > 0;
			super.state(ownerIsMember, "*", "acme.validation.project.component-owner-not-member.message");
			isValid &= ownerIsMember;

			if (Boolean.FALSE.equals(strategy.getDraftMode()))
				continue;

			tactics = this.repository.countTacticsByStrategyId(strategy.getId());
			publishable = tactics != null && tactics > 0 && this.isFutureInterval(strategy.getStartMoment(), strategy.getEndMoment(), now);
			isValid &= publishable;
		}

		return isValid;
	}

	private boolean isFutureInterval(final Date start, final Date end, final Date now) {
		assert now != null;

		return start != null && end != null && MomentHelper.isAfter(start, now) && MomentHelper.isAfter(end, now) && MomentHelper.isBefore(start, end);
	}

}
