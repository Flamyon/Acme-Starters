
package acme.features.student3.fundraiser.strategy;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student3.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyListService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repo;

	private Collection<Strategy>			strategyCollection;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int ownerRealmId;

		ownerRealmId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.strategyCollection = this.repo.findStrategyByFundraiserId(ownerRealmId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.strategyCollection, "ticker", "name", "startMoment");
	}

}
