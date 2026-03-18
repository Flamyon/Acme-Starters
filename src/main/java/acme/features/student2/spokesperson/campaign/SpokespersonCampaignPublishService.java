
package acme.features.student2.spokesperson.campaign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

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
	}

	@Override
	public void validate() {
		int id = super.getRequest().getData("id", int.class);
		boolean hasMilestone = this.repo.countMilestonesFromCampaignId(id) > 0;
		super.state(hasMilestone, "*", "acme.validation.campaign.no-milestones.message");
		Date start = this.entity.getStartMoment();
		Date end = this.entity.getEndMoment();
		if (start != null)
			super.state(MomentHelper.isFuture(start), "startMoment", "acme.validation.campaign.start-not-future.message");
		if (end != null)
			super.state(MomentHelper.isFuture(end), "endMoment", "acme.validation.campaign.end-not-future.message");
		if (start != null && end != null)
			super.state(MomentHelper.isBefore(start, end), "endMoment", "acme.validation.campaign.invalid-interval.message");
	}
	@Override
	public void execute() {
		this.entity.setDraftMode(false);
		this.repo.save(this.entity);
	}
	@Override
	public void unbind() {
		Tuple t = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		t.put("monthsActive", this.entity.getMonthsActive());
		t.put("effort", this.entity.getEffort());
	}
}
