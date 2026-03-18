
package acme.features.student2.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student2.Milestone;
import acme.entities.student2.Spokesperson;

@Service
public class SpokespersonMilestoneDeleteService extends AbstractService<Spokesperson, Milestone> {

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
		super.bindObject(this.entity);
	}

	@Override
	public void validate() {
		super.validateObject(this.entity);
	}
	@Override
	public void execute() {
		this.repo.delete(this.entity);
	}
}
