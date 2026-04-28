
package acme.features.student5.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportShowService extends AbstractService<Auditor, AuditReport> {

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

		status = this.entity != null && this.entity.getId() != 0 && //
			(this.entity.getAuditor() != null && this.entity.getAuditor().isPrincipal() || Boolean.FALSE.equals(this.entity.getDraftMode()));

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean isOwner;
		Tuple tuple;

		tuple = super.unbindObject(this.entity, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("id", this.entity.getId() != 0 ? this.entity.getId() : 0);
		tuple.put("monthsActive", this.entity.getMonthsActive());
		tuple.put("hours", this.entity.getHours());
		isOwner = this.entity.getAuditor() != null && this.entity.getAuditor().isPrincipal();
		tuple.put("isOwner", isOwner);
		tuple.put("canAssociateProject", isOwner && Boolean.FALSE.equals(this.entity.getDraftMode()));
	}

}
