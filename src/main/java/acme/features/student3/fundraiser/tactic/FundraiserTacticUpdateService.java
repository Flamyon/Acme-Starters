
package acme.features.student3.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student3.Tactic;
import acme.entities.student3.TacticKind;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticUpdateService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private FundraiserTacticRepository	repo;

	private Tactic						entityTactic;


	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entityTactic = this.repo.findTacticById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entityTactic != null && this.entityTactic.getStrategy().getDraftMode() && this.entityTactic.getStrategy().getFundraiser().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.entityTactic, "name", "notes", "expectedPercentage", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.entityTactic);
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
		tuple.put("draftMode", this.entityTactic.getStrategy().getDraftMode());
	}
}
