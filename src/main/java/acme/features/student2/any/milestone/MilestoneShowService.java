
package acme.features.student2.any.milestone;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;

public class MilestoneShowService extends AbstractService<Any, Milestone> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private MilestoneRepository	repository;

	private Milestone			milestone;

	private Campaign			campaign;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
		if (this.milestone != null)
			this.campaign = this.milestone.getCampaign();
	}

	@Override
	public void authorise() {

		super.setAuthorised(this.milestone != null && !this.campaign.getDraftMode());
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		int campaignId = this.milestone.getCampaign().getId();
		tuple.put("campaignId", campaignId);

	}

}
