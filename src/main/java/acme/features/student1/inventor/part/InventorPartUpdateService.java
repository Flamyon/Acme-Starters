package acme.features.student1.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student1.Inventor;
import acme.entities.student1.Part;
import acme.entities.student1.PartKind;

@Service
public class InventorPartUpdateService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository repo;

	private Part entityPart;


	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entityPart = this.repo.findPartById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entityPart != null && this.entityPart.getInvention().getDraftMode() && this.entityPart.getInvention().getInventor().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.entityPart, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.entityPart);
	}

	@Override
	public void execute() {
		this.repo.save(this.entityPart);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices;

		choices = SelectChoices.from(PartKind.class, this.entityPart.getKind());
		tuple = super.unbindObject(this.entityPart, "name", "description", "cost", "kind");
		tuple.put("kind", this.entityPart.getKind());
		tuple.put("choices", choices);
		tuple.put("draftMode", this.entityPart.getInvention().getDraftMode());
	}
}
