
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
	private AuditorAuditSectionRepository	repository;

	private AuditSection					auditSection;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int auditReportId;
		AuditReport auditReport;

		auditReportId = super.getRequest().getData("auditReportId", int.class);
		auditReport = this.repository.findAuditReportById(auditReportId);

		this.auditSection = super.newObject(AuditSection.class);
		this.auditSection.setAuditReport(auditReport);
	}

	@Override
	public void authorise() {
		boolean status;

		// Parent sponsorship must exist, be in draft, and belong to this sponsor
		status = this.auditSection.getAuditReport() != null && //
			this.auditSection.getAuditReport().getDraftMode() && //
			this.auditSection.getAuditReport().getAuditor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.auditSection, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.auditSection);
	}

	@Override
	public void execute() {
		this.repository.save(this.auditSection);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(SectionKind.class, this.auditSection.getKind());

		tuple = super.unbindObject(this.auditSection, "name", "notes", "hours", "kind");
		tuple.put("auditReportId", super.getRequest().getData("auditReportId", int.class));
		tuple.put("draftMode", this.auditSection.getAuditReport().getDraftMode());
		tuple.put("choices", choices);

	}
}
