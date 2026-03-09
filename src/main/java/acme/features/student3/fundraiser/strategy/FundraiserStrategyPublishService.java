
package acme.features.student3.fundraiser.strategy;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.student3.Strategy;
import acme.entities.student3.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyPublishService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repo;

	private Strategy						entity;


	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity = this.repo.findStrategyById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entity != null && this.entity.getDraftMode() && this.entity.getFundraiser().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		// nothing to bind; publishing only changes draftMode
	}

	@Override
	public void validate() {
		int entityId;
		boolean hasTactic;
		Date start, end;

		entityId = super.getRequest().getData("id", int.class);

		// 1. Must have at least one tactic
		hasTactic = this.repo.countTacticsFromStrategyId(entityId) > 0;
		super.state(hasTactic, "*", "acme.validation.strategy.no-tactics.message");

		// 2. startMoment and endMoment must be in the future at publish time
		start = this.entity.getStartMoment();
		end = this.entity.getEndMoment();

		if (start != null)
			super.state(MomentHelper.isFuture(start), "startMoment", "acme.validation.strategy.start-not-future.message");

		if (end != null)
			super.state(MomentHelper.isFuture(end), "endMoment", "acme.validation.strategy.end-not-future.message");

		// 3. startMoment must be before endMoment
		if (start != null && end != null)
			super.state(MomentHelper.isBefore(start, end), "endMoment", "acme.validation.strategy.invalid-interval.message");
	}

	@Override
	public void execute() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity.setDraftMode(false);

		Collection<Tactic> tactics = this.repo.findTacticsByStrategyId(entityId);
		tactics.stream().forEach(t -> t.getStrategy().setDraftMode(false));

		this.repo.saveAll(tactics);
		this.repo.save(this.entity);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("expectedPercentage", this.entity.getExpectedPercentage());
	}
}
