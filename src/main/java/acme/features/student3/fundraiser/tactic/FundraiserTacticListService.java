
package acme.features.student3.fundraiser.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student3.Tactic;
import acme.entities.student3.Strategy;
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
		boolean status = false;
		Strategy strategy;
		Fundraiser fundraiser;

		strategy = this.repo.findStrategyById(super.getRequest().getData("strategyId", int.class));
		fundraiser = (Fundraiser) super.getRequest().getPrincipal().getActiveRealm();
		if (strategy != null && strategy.getFundraiser() != null && strategy.getFundraiser().getId() == fundraiser.getId())
			status = true;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		int strategyId;

		strategyId = super.getRequest().getData("strategyId", int.class);
		super.unbindObjects(this.tacticCollection, "name", "expectedPercentage", "kind");
		super.unbindGlobal("strategyId", strategyId);

		Strategy strategy = this.repo.findStrategyById(strategyId);
		boolean draft = false;
		if (strategy != null) {
			Boolean dm = strategy.getDraftMode();
			draft = (dm != null) ? dm : false;
		}

		super.unbindGlobal("draftMode", draft);
	}
}
