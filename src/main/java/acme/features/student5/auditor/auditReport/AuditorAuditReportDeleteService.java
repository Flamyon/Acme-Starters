
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
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity = this.repo.findAuditReportById(entityId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.entity != null && this.entity.getDraftMode() && this.entity.getAuditor().isPrincipal();

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

		auditSections = this.repo.findAuditSectionsByAuditReportId(this.entity.getId());
		this.repo.deleteAll(auditSections);
		this.repo.delete(this.entity);
	}
}
