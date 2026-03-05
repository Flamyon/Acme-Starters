
package acme.entities.student5;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class AuditReportValidator extends AbstractValidator<ValidAuditReport, AuditSection> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditReportRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAuditReport annotation) {
		assert annotation != null;
	}

	public boolean isValid(final AuditReport auditreport, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (auditreport == null)
			result = true;
		else {

			if (auditreport.getDraftMode() != null && !auditreport.getDraftMode()) {
				boolean hasAuditSections;

				if (auditreport.getId() == 0)
					hasAuditSections = false;
				else {
					Long count = this.repository.countAuditSections(auditreport.getId());
					hasAuditSections = count != null && count > 0;
				}

				super.state(context, hasAuditSections, "draftMode", "acme.validation.auditReport.no-auditsections.message");
			}

			if (auditreport.getStartMoment() != null && auditreport.getEndMoment() != null) {

				boolean validInterval = auditreport.getStartMoment().before(auditreport.getEndMoment());
				super.state(context, validInterval, "endMoment", "acme.validation.campaign.invalid-interval.message");

				if (auditreport.getDraftMode() != null && !auditreport.getDraftMode()) {
					Date now = new Date();
					boolean futureStart = auditreport.getStartMoment().after(now);

					super.state(context, futureStart, "startMoment", "acme.validation.auditreport.past-start-moment.message");
				}
			}

			result = !super.hasErrors(context);
		}

		return result;
	}

	@Override
	public boolean isValid(final AuditSection value, final ConstraintValidatorContext context) {
		return false;
	}

}
