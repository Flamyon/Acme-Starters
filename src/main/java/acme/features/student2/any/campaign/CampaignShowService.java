
package acme.features.student2.any.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;

@Service
public class CampaignShowService extends AbstractService<Any, Campaign> {

	@Autowired
	private CampaignRepository	repository;

	private Campaign			campaign;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.campaign != null && !this.campaign.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.campaign.getMonthsActive());
		tuple.put("effort", this.campaign.getEffort());
		tuple.put("spokespersonId", this.campaign.getSpokesperson().getId());
	}

}
