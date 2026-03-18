
package acme.features.student2.spokesperson.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonCampaignDeleteService extends AbstractService<Spokesperson, Campaign> {

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
		super.bindObject(this.entity, "ticker");
	}
	@Override
	public void validate() {
		super.validateObject(this.entity);
	}
	@Override
	public void execute() {
		Collection<Milestone> ms = this.repo.findMilestonesByCampaignId(this.entity.getId());
		this.repo.deleteAll(ms);
		this.repo.delete(this.entity);
	}
}
