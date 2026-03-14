package acme.features.student1.inventor.invention;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;
import acme.entities.student1.Part;

@Service
public class InventorInventionDeleteService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository repo;

	private Invention entity;

	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity = this.repo.findInventionById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entity != null && this.entity.getDraftMode() && this.entity.getInventor().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.entity, "ticker");
	}

	@Override
	public void validate() {
		super.validateObject(this.entity);
	}

	@Override
	public void execute() {
		Collection<Part> parts;

		parts = this.repo.findPartsByInventionId(this.entity.getId());
		this.repo.deleteAll(parts);
		this.repo.delete(this.entity);
	}
}
