
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
public class SpokespersonMilestoneUpdateService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repo;
	private Milestone						entity;


	@Override
	public void load() {
		this.entity = this.repo.findMilestoneById(super.getRequest().getData("id", int.class));
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.entity != null && this.entity.getCampaign().getDraftMode() && this.entity.getCampaign().getSpokesperson().isPrincipal());
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
		t.put("draftMode", this.entity.getCampaign().getDraftMode());
	}
}
