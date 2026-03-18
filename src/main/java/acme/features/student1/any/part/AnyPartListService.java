package acme.features.student1.any.part;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Part;

@Service
public class AnyPartListService extends AbstractService<Any, Part> {

	@Autowired
	private AnyPartRepository repository;

	private Invention invention;
	private Collection<Part> parts;


	@Override
	public void load() {
		int inventionId;

		inventionId = super.getRequest().getData("inventionId", int.class);
		this.invention = this.repository.findInventionById(inventionId);
		this.parts = this.repository.findPartsByInventionId(inventionId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && !this.invention.getDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		int inventionId;

		inventionId = super.getRequest().getData("inventionId", int.class);
		super.unbindObjects(this.parts, "name", "description", "cost", "kind");
		super.unbindGlobal("inventionId", inventionId);
	}
}
