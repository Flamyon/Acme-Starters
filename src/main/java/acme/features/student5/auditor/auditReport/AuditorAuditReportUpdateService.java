
package acme.features.student5.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
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

		// TICKER
		if (!super.getErrors().hasErrors("ticker")) {

			String ticker = this.entity.getTicker();

			if (ticker != null && !ticker.trim().isEmpty()) {
				AuditReport other = this.repo.findAuditReportByTicker(ticker);

				boolean isDuplicate = other != null;

				super.state(!isDuplicate, "ticker", "acme.validation.audit-report.ticker-duplicated.message");
			}
		}

		// FECHAS
		if (!super.getErrors().hasErrors("startMoment"))
			super.state(this.entity.getStartMoment() != null, "startMoment", "acme.validation.audit-report.start-null.message");

		if (!super.getErrors().hasErrors("endMoment"))
			super.state(this.entity.getEndMoment() != null, "endMoment", "acme.validation.audit-report.end-null.message");

		if (!super.getErrors().hasErrors("startMoment") && !super.getErrors().hasErrors("endMoment"))
			if (this.entity.getStartMoment() != null && this.entity.getEndMoment() != null)
				super.state(MomentHelper.isBefore(this.entity.getStartMoment(), this.entity.getEndMoment()), "endMoment", "acme.validation.audit-report.invalid-interval.message");
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
