
package acme.features.student2.spokesperson.milestone;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonMilestoneListService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repo;
	private Collection<Milestone>			milestones;
	private Campaign						campaign;


	@Override
	public void load() {
		int campaignId = super.getRequest().getData("campaignId", int.class);
		this.milestones = this.repo.findMilestonesByCampaignId(campaignId);
		this.campaign = this.repo.findCampaignById(campaignId);
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.campaign != null && this.campaign.getSpokesperson().isPrincipal());
	}
	@Override
	public void unbind() {
		int campaignId = super.getRequest().getData("campaignId", int.class);
		super.unbindObjects(this.milestones, "title", "effort", "kind");
		super.unbindGlobal("campaignId", campaignId);
		super.unbindGlobal("draftMode", this.campaign.getDraftMode());
	}
}
