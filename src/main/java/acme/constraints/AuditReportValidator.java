
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.student5.AuditReport;
import acme.entities.student5.AuditReportRepository;

@Validator
public class AuditReportValidator extends AbstractValidator<ValidAuditReport, AuditReport> {

	@Autowired
	private AuditReportRepository repository;


	@Override
	public void initialize(final ValidAuditReport constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final AuditReport auditReport, final ConstraintValidatorContext context) {
		assert context != null;
		if (auditReport == null)
			return true;

		{
			AuditReport existingAuditReport = this.repository.findByTicker(auditReport.getTicker());
			boolean uniqueAuditReport = existingAuditReport == null || existingAuditReport.getId() == auditReport.getId();
			super.state(context, uniqueAuditReport, "ticker", "acme.validation.audit-report.duplicated-ticker.message");
		}
		{
			Date start = auditReport.getStartMoment();
			Date end = auditReport.getEndMoment();

			if (start != null && end != null) {
				boolean correctOrder = end.after(start);
				super.state(context, correctOrder, "endMoment", "acme.validation.audit-report.invalid-date-order.message");
			}
		}

		this.validateProjectPublicationConsistency(auditReport, context);

		if (auditReport.getDraftMode() != null && !auditReport.getDraftMode()) {
			{
				Date now = MomentHelper.getBaseMoment();
				Date start = auditReport.getStartMoment();

				if (start != null) {
					boolean futureStart = start.after(now);
					super.state(context, futureStart, "startMoment", "acme.validation.audit-report.date-not-future.message");
				}
			}
			{
				Integer existingSections = this.repository.countAuditSectionsByAuditReportId(auditReport.getId());
				boolean hasSection = existingSections != null && existingSections > 0;
				super.state(context, hasSection, "draftMode", "acme.validation.audit-report.missing-donations.message");
			}
		}

		return !super.hasErrors(context);
	}

	private boolean validateProjectPublicationConsistency(final AuditReport auditReport, final ConstraintValidatorContext context) {
		boolean isValid;
		boolean auditReportPublished;
		boolean projectPublished;

		if (auditReport.getProject() == null)
			return true;

		isValid = true;

		auditReportPublished = Boolean.FALSE.equals(auditReport.getDraftMode());
		super.state(context, auditReportPublished, "project", "acme.validation.audit-report.project-unpublished-audit-report.message");
		isValid &= auditReportPublished;

		projectPublished = auditReport.getProject().getDraftMode() != null && !auditReport.getProject().getDraftMode();
		super.state(context, projectPublished, "project", "acme.validation.audit-report.project-unpublished-project.message");
		isValid &= projectPublished;

		return isValid;
	}
}
