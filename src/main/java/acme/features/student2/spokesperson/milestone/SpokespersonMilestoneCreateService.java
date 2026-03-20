
package acme.features.student2.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student2.Campaign;
import acme.entities.student2.Milestone;
import acme.entities.student2.MilestoneKind;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonMilestoneCreateService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repo;
	private Campaign						parent;
	private Milestone						entity;


	@Override
	public void load() {
		int campaignId = super.getRequest().getData("campaignId", int.class);
		this.parent = this.repo.findCampaignById(campaignId);
		this.entity = this.newObject(Milestone.class);
		this.entity.setCampaign(this.parent);
	}
	@Override
	public void authorise() {
		boolean isSpokesperson = super.getRequest().getPrincipal().getActiveRealm().getClass() == Spokesperson.class;

		boolean isValidCampaign = this.parent != null && this.parent.getDraftMode() && this.parent.getSpokesperson().isPrincipal();

		super.setAuthorised(isSpokesperson && isValidCampaign);
	}
	@Override
	public void bind() {
		super.bindObject(this.entity, "title", "achievements", "effort", "kind");
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
		SelectChoices choices = SelectChoices.from(MilestoneKind.class, this.entity.getKind());
		Tuple t = super.unbindObject(this.entity, "title", "achievements", "effort", "kind");
		t.put("kind", this.entity.getKind());
		t.put("choices", choices);
		t.put("campaignId", this.entity.getCampaign().getId());
	}
}
