
package acme.features.student2.any.milestone;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student2.Milestone;

public class MilestoneShowService extends AbstractService<Any, Milestone> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private MilestoneRepository	repository;

	private Milestone			milestone;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.milestone != null;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		int campaignId = this.milestone.getCampaign().getId();
		tuple.put("campaignId", campaignId);

	}

}
