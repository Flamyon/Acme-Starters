
package acme.features.student5.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.student5.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionDeleteService extends AbstractService<Auditor, AuditSection> {

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
		super.bindObject(this.entityAuditSection);
	}

	@Override
	public void validate() {
		super.validateObject(this.entityAuditSection);
	}

	@Override
	public void execute() {
		this.repo.delete(this.entityAuditSection);
	}
}
