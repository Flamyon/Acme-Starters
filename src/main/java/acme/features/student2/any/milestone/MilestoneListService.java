
package acme.features.student2.any.milestone;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;

@Service
public class MilestoneListService extends AbstractService<Any, Milestone> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private MilestoneRepository		repository;

	private Campaign				campaign;
	private Collection<Milestone>	milestones;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int campaignId;

		campaignId = super.getRequest().getData("campaignId", int.class);
		this.milestones = this.repository.findMilestonesByCampaignId(campaignId);
		this.campaign = this.repository.findCampaignById(campaignId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.campaign != null && !this.campaign.getDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestones, "title", "achievements", "effort", "kind");
	}

}
