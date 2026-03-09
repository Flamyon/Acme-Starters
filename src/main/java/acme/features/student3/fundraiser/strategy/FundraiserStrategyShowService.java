
package acme.features.student3.fundraiser.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
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

		status = this.entity != null && //
			(this.entity.getFundraiser().isPrincipal() || !this.entity.getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("expectedPercentage", this.entity.getExpectedPercentage());
	}

}
