package acme.features.student1.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;
import acme.entities.student1.Part;
import acme.entities.student1.PartKind;

@Service
public class InventorPartCreateService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository repo;

	private Invention parentInvention;
	private Part entityPart;


	@Override
	public void load() {
		int inventionId;

		inventionId = super.getRequest().getData("inventionId", int.class);
		this.parentInvention = this.repo.findInventionById(inventionId);
		this.entityPart = this.newObject(Part.class);
		this.entityPart.setInvention(this.parentInvention);
		this.entityPart.getInvention().setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Inventor.class);
	}

	@Override
	public void bind() {
		super.bindObject(this.entityPart, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
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
		tuple.put("inventionId", this.entityPart.getInvention().getId());
	}
}
