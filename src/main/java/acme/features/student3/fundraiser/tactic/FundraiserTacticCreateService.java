
package acme.features.student3.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student3.Strategy;
import acme.entities.student3.Tactic;
import acme.entities.student3.TacticKind;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticCreateService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private FundraiserTacticRepository	repo;

	private Strategy					parentStrategy;
	private Tactic					entityTactic;


	@Override
	public void load() {
		int strategyId = super.getRequest().getData("strategyId", int.class);

		this.parentStrategy = this.repo.findStrategyById(strategyId);

		this.entityTactic = this.newObject(Tactic.class);
		this.entityTactic.setStrategy(this.parentStrategy);
		this.entityTactic.getStrategy().setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Fundraiser.class);
	}

	@Override
	public void bind() {
		super.bindObject(this.entityTactic, "name", "notes", "expectedPercentage", "kind");
	}

	@Override
	public void validate() {
	}

	@Override
	public void execute() {
		this.repo.save(this.entityTactic);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices = SelectChoices.from(TacticKind.class, this.entityTactic.getKind());

		tuple = super.unbindObject(this.entityTactic, "name", "notes", "expectedPercentage", "kind");
		tuple.put("kind", this.entityTactic.getKind());
		tuple.put("choices", choices);
		tuple.put("strategyId", this.entityTactic.getStrategy().getId());
	}
}
