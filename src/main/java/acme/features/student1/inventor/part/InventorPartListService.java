package acme.features.student1.inventor.part;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student1.Inventor;
import acme.entities.student1.Part;

@Service
public class InventorPartListService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository repo;

	private Collection<Part> partCollection;

	@Override
	public void load() {
		int inventionId;

		inventionId = super.getRequest().getData("inventionId", int.class);
		this.partCollection = this.repo.findPartsByInventionId(inventionId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		int inventionId;

		inventionId = super.getRequest().getData("inventionId", int.class);
		super.unbindObjects(this.partCollection, "name", "cost", "kind");
		super.unbindGlobal("inventionId", inventionId);
	}
}
