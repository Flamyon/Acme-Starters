
package acme.features.student2.spokesperson.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonCampaignListService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repo;
	private Collection<Campaign>			campaigns;


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}
	@Override
	public void load() {
		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.campaigns = this.repo.findCampaignsBySpokespersonId(id);
	}
	@Override
	public void unbind() {
		super.unbindObjects(this.campaigns, "ticker", "name", "startMoment", "project.title");
	}
}
