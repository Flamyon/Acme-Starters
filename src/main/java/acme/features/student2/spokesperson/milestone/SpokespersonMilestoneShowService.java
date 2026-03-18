
package acme.features.student2.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student2.Milestone;
import acme.entities.student2.MilestoneKind;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonMilestoneShowService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repo;
	private Milestone						entity;


	@Override
	public void load() {
		this.entity = this.repo.findMilestoneById(super.getRequest().getData("id", int.class));
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.entity != null && this.entity.getCampaign().getSpokesperson().isPrincipal());
	}
	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(MilestoneKind.class, this.entity.getKind());
		Tuple t = super.unbindObject(this.entity, "title", "achievements", "effort");
		t.put("kind", this.entity.getKind().toString());
		t.put("choices", choices);
		t.put("campaignId", this.entity.getCampaign().getId());
		t.put("draftMode", this.entity.getCampaign().getDraftMode());
	}
}
