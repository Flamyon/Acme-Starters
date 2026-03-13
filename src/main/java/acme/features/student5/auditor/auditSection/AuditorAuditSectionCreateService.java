
package acme.features.student5.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditReport;
import acme.entities.student5.AuditSection;
import acme.entities.student5.SectionKind;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionCreateService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repo;

	private AuditReport						parentAuditReport;
	private AuditSection					entityAuditSection;


	@Override
	public void load() {
		int auditReportId = super.getRequest().getData("auditReportId", int.class);

		this.parentAuditReport = this.repo.findAuditReportById(auditReportId);

		this.entityAuditSection = this.newObject(AuditSection.class);
		this.entityAuditSection.setAuditReport(this.parentAuditReport);
		this.entityAuditSection.getAuditReport().setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass() == Auditor.class);
	}

	@Override
	public void bind() {
		super.bindObject(this.entityAuditSection, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
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
		tuple.put("auditReportId", this.entityAuditSection.getAuditReport().getId());
	}
}
