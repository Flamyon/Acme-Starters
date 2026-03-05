
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.student4.Sponsorship;
import acme.entities.student4.SponsorshipRepository;

@Validator
public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	@Autowired
	private SponsorshipRepository repository;


	@Override
	public void initialize(final ValidSponsorship constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship sponsorship, final ConstraintValidatorContext context) {
		assert context != null;

		if (sponsorship == null)
			return true;
		{
			Sponsorship existingSponsorship = this.repository.findSponsorshipByTicker(sponsorship.getTicker());
			boolean uniqueSponsorship = existingSponsorship == null || existingSponsorship.getId() == sponsorship.getId();

			super.state(context, uniqueSponsorship, "ticker", "acme.validation.sponsorship.duplicated-ticker.message");
		}

		if (sponsorship.getDraftMode() != null && !sponsorship.getDraftMode()) {

			{
				Date now = MomentHelper.getBaseMoment();
				Date start = sponsorship.getStartMoment();
				Date end = sponsorship.getEndMoment();

				if (start != null && end != null) {
					boolean correctOrder = end.after(start);
					super.state(context, correctOrder, "endMoment", "acme.validation.sponsorship.invalid-date-order.message");

					boolean futureStart = start.after(now);
					super.state(context, futureStart, "startMoment", "acme.validation.sponsorship.date-not-future.message");
				}
			}

			{
				Integer existingDonations = this.repository.countDonationsBySponsorshipId(sponsorship.getId());
				boolean hasDonation = existingDonations != null && existingDonations > 0;

				super.state(context, hasDonation, "draftMode", "acme.validation.sponsorship.missing-donations.message");
			}
		}

		return !super.hasErrors(context);
	}
}
