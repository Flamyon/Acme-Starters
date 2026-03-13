
package acme.features.student5.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportUpdateService extends AbstractService<Auditor, AuditReport> {

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
		super.bindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
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
		Tuple tuple;

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("hours", this.entity.getHours());
	}
}
