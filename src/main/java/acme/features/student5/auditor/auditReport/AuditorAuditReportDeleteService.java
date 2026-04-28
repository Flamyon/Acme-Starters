
package acme.features.student5.auditor.auditReport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student5.AuditReport;
import acme.entities.student5.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportDeleteService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository	repo;

	private AuditReport						entity;


	@Override
	public void load() {
		Integer entityId;

		entityId = super.getRequest().getData("id", Integer.class, null);
		if (entityId == null || entityId.intValue() == 0)
			this.entity = super.newObject(AuditReport.class);
		else {
			this.entity = this.repo.findAuditReportById(entityId.intValue());
			if (this.entity == null)
				this.entity = super.newObject(AuditReport.class);
		}
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entity != null && this.entity.getId() != 0 && Boolean.TRUE.equals(this.entity.getDraftMode()) && this.entity.getAuditor() != null && this.entity.getAuditor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.entity, "ticker");
	}

	@Override
	public void validate() {
		super.validateObject(this.entity);
	}

	@Override
	public void execute() {
		Collection<AuditSection> auditSections;

		if (this.entity.getId() == 0)
			return;

		auditSections = this.repo.findAuditSectionsByAuditReportId(this.entity.getId());
		this.repo.deleteAll(auditSections);
		this.repo.delete(this.entity);
	}
}
