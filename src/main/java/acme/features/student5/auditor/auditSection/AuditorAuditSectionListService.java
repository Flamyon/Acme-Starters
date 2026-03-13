
package acme.features.student5.auditor.auditSection;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student5.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionListService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repo;

	private Collection<AuditSection>		auditSectionCollection;


	@Override
	public void load() {
		int auditReportId;

		auditReportId = super.getRequest().getData("auditReportId", int.class);
		this.auditSectionCollection = this.repo.findAuditSectionsByAuditReportId(auditReportId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		int auditReportId;

		auditReportId = super.getRequest().getData("auditReportId", int.class);
		super.unbindObjects(this.auditSectionCollection, "name", "hours", "kind");
		super.unbindGlobal("auditReportId", auditReportId);

	}
}
