
package acme.features.student3.fundraiser.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student3.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticListService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private FundraiserTacticRepository	repo;

	private Collection<Tactic>			tacticCollection;

	@Override
	public void load() {
		int strategyId;

		strategyId = super.getRequest().getData("strategyId", int.class);
		this.tacticCollection = this.repo.findTacticsByStrategyId(strategyId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		int strategyId;

		strategyId = super.getRequest().getData("strategyId", int.class);
		super.unbindObjects(this.tacticCollection, "name", "expectedPercentage", "kind");
		super.unbindGlobal("strategyId", strategyId);

	}
}
