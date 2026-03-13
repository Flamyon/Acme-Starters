
package acme.features.student5.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditSection;
import acme.entities.student5.SectionKind;
import acme.features.student5.any.auditSection.AnyAuditSectionRepository;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionShowService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AnyAuditSectionRepository	repo;

	private AuditSection				entityAuditSection;


	@Override
	public void load() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entityAuditSection = this.repo.findAuditSectionById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entityAuditSection != null;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.entityAuditSection.getKind());

		Tuple tuple = super.unbindObject(this.entityAuditSection, "name", "notes", "hours");

		tuple.put("kind", this.entityAuditSection.getKind().toString());
		tuple.put("choices", choices);
		tuple.put("auditReportId", this.entityAuditSection.getAuditReport().getId());
		tuple.put("draftMode", this.entityAuditSection.getAuditReport().getDraftMode());
	}
}
