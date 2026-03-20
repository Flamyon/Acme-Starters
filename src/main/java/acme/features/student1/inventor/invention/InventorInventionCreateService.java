package acme.features.student1.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Service
public class InventorInventionCreateService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository repo;
	private Invention entity;


	@Override
	public void load() {
		Inventor inventor;

		inventor = (Inventor) super.getRequest().getPrincipal().getActiveRealm();
		this.entity = this.newObject(Invention.class);
		this.entity.setInventor(inventor);
		this.entity.setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Inventor.class);
	}

	@Override
	public void bind() {
		super.bindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.entity);
	}

	@Override
	public void execute() {
		this.repo.save(this.entity);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("cost", this.entity.getCost(this.entity.getId()));
	}
}
