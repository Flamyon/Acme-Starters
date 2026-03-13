
package acme.features.student3.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student3.Tactic;
import acme.entities.student3.TacticKind;
import acme.features.student3.any.tactic.AnyTacticRepository;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticShowService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private AnyTacticRepository	repo;

	private Tactic				entityTactic;


	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entityTactic = this.repo.findTacticById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entityTactic != null;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(TacticKind.class, this.entityTactic.getKind());

		Tuple tuple = super.unbindObject(this.entityTactic, "name", "notes", "expectedPercentage");

		tuple.put("kind", this.entityTactic.getKind().toString());
		tuple.put("choices", choices);
		tuple.put("strategyId", this.entityTactic.getStrategy().getId());
		tuple.put("draftMode", this.entityTactic.getStrategy().getDraftMode());
	}
}
