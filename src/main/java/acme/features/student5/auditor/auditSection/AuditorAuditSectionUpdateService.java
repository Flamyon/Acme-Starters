
package acme.features.student5.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditSection;
import acme.entities.student5.SectionKind;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionUpdateService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repo;
	private AuditSection					entity;


	@Override
	public void load() {
		this.entity = this.repo.findAuditSectionById(super.getRequest().getData("id", int.class));
	}
	@Override
	public void authorise() {
		super.setAuthorised(this.entity != null && this.entity.getAuditReport().getDraftMode() && this.entity.getAuditReport().getAuditor().isPrincipal());
	}
	@Override
	public void bind() {
		super.bindObject(this.entity, "name", "notes", "hours", "kind");
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
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.entity.getKind());
		Tuple t = super.unbindObject(this.entity, "name", "notes", "hours", "kind");
		t.put("kind", this.entity.getKind());
		t.put("choices", choices);
		t.put("draftMode", this.entity.getAuditReport().getDraftMode());
	}
}
