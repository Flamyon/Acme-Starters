
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

	private AuditSection					entityAuditSection;


	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entityAuditSection = this.repo.findAuditSectionById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entityAuditSection != null && this.entityAuditSection.getAuditReport().getDraftMode() && this.entityAuditSection.getAuditReport().getAuditor().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.entityAuditSection, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.entityAuditSection);
	}

	@Override
	public void execute() {
		this.repo.save(this.entityAuditSection);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.entityAuditSection.getKind());

		tuple = super.unbindObject(this.entityAuditSection, "name", "notes", "hours", "kind");
		tuple.put("kind", this.entityAuditSection.getKind());
		tuple.put("choices", choices);
		tuple.put("draftMode", this.entityAuditSection.getAuditReport().getDraftMode());
	}
}
