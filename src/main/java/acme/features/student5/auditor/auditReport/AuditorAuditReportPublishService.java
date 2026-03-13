
package acme.features.student5.auditor.auditReport;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.student5.AuditReport;
import acme.entities.student5.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportPublishService extends AbstractService<Auditor, AuditReport> {

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

	}

	@Override
	public void validate() {
		int entityId;
		boolean hasAuditSection;
		Date start, end;

		entityId = super.getRequest().getData("id", int.class);

		hasAuditSection = this.repo.countAuditSectionsFromAuditReportId(entityId) > 0;
		super.state(hasAuditSection, "*", "acme.validation.auditReport.no-auditSections.message");

		start = this.entity.getStartMoment();
		end = this.entity.getEndMoment();

		if (start != null)
			super.state(MomentHelper.isFuture(start), "startMoment", "acme.validation.auditReport.start-not-future.message");

		if (end != null)
			super.state(MomentHelper.isFuture(end), "endMoment", "acme.validation.auditReport.end-not-future.message");

		if (start != null && end != null)
			super.state(MomentHelper.isBefore(start, end), "endMoment", "acme.validation.auditReport.invalid-interval.message");
	}

	@Override
	public void execute() {
		int entityId;

		entityId = super.getRequest().getData("id", int.class);
		this.entity.setDraftMode(false);

		Collection<AuditSection> auditSections = this.repo.findAuditSectionsByAuditReportId(entityId);
		auditSections.stream().forEach(t -> t.getAuditReport().setDraftMode(false));

		this.repo.saveAll(auditSections);
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
