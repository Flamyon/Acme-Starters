
package acme.features.student3.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student3.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticDeleteService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private FundraiserTacticRepository	repo;

	private Tactic					entityTactic;

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
		super.bindObject(this.entityTactic);
	}

	@Override
	public void validate() {
		super.validateObject(this.entityTactic);
	}

	@Override
	public void execute() {
		this.repo.delete(this.entityTactic);
	}
}
