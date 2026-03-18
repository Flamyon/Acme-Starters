package acme.features.student1.inventor.invention;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.student1.Invention;
import acme.entities.student1.Inventor;

@Service
public class InventorInventionPublishService extends AbstractService<Inventor, Invention> {

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
	}

	@Override
	public void validate() {
		int entityId;
		boolean hasPart;
		Date start, end;

		entityId = super.getRequest().getData("id", int.class);
		hasPart = this.repo.countPartsFromInventionId(entityId) > 0;
		super.state(hasPart, "*", "acme.validation.invention.no-parts.message");

		start = this.entity.getStartMoment();
		end = this.entity.getEndMoment();

		if (start != null)
			super.state(MomentHelper.isFuture(start), "startMoment", "acme.validation.invention.start-not-future.message");

		if (end != null)
			super.state(MomentHelper.isFuture(end), "endMoment", "acme.validation.invention.end-not-future.message");

		if (start != null && end != null)
			super.state(MomentHelper.isBefore(start, end), "endMoment", "acme.validation.invention.invalid-interval.message");
	}

	@Override
	public void execute() {
		this.entity.setDraftMode(false);
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
