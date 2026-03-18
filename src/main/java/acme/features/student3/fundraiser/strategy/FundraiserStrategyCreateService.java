
package acme.features.student3.fundraiser.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
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
		super.bindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.entity);

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

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("expectedPercentage", this.entity.getExpectedPercentage());
	}
}
