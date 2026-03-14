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
public class InventorPartShowService extends AbstractService<Inventor, Part> {

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

		status = this.entityPart != null;
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(PartKind.class, this.entityPart.getKind());
		tuple = super.unbindObject(this.entityPart, "name", "description", "cost");
		tuple.put("kind", this.entityPart.getKind().toString());
		tuple.put("choices", choices);
		tuple.put("inventionId", this.entityPart.getInvention().getId());
		tuple.put("draftMode", this.entityPart.getInvention().getDraftMode());
	}
}
