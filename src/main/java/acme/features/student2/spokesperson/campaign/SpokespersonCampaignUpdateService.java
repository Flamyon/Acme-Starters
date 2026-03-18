
package acme.features.student2.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonCampaignUpdateService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repo;
	private Campaign						entity;


	@Override
	public void load() {
		this.entity = this.repo.findCampaignById(super.getRequest().getData("id", int.class));
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.entity != null && this.entity.getDraftMode() && this.entity.getSpokesperson().isPrincipal());
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
		Tuple t = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		t.put("monthsActive", this.entity.getMonthsActive());
		t.put("effort", this.entity.getEffort());
	}
}
