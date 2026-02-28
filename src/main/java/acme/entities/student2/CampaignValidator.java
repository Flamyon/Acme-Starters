
package acme.entities.student2;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CampaignRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
		// HINT: campaign can be null
		assert context != null;

		boolean result;

		if (campaign == null)
			result = true;
		else {

			// REGLA 1: Campaigns cannot be published unless they have at least one milestone.
			if (campaign.getDraftMode() != null && !campaign.getDraftMode()) {
				boolean hasMilestones;

				// Ojo: Si la campaña es nueva (id == 0), no tiene hitos guardados todavía.
				if (campaign.getId() == 0)
					hasMilestones = false;
				else {
					Long count = this.repository.countMilestones(campaign.getId());
					hasMilestones = count != null && count > 0;
				}

				super.state(context, hasMilestones, "draftMode", "acme.validation.campaign.no-milestones.message");
			}

			// REGLA 2: startMoment/endMoment must be a valid time interval in future wrt. publication moment
			if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {

				// 2A: Valid time interval (El inicio debe ser anterior al fin)
				boolean validInterval = campaign.getStartMoment().before(campaign.getEndMoment());
				super.state(context, validInterval, "endMoment", "acme.validation.campaign.invalid-interval.message");

				// 2B: In future wrt the moment when a campaign is published
				// Comprobamos que al publicar, la fecha de inicio no se haya quedado en el pasado
				if (campaign.getDraftMode() != null && !campaign.getDraftMode()) {
					Date now = new Date();
					boolean futureStart = campaign.getStartMoment().after(now);

					super.state(context, futureStart, "startMoment", "acme.validation.campaign.past-start-moment.message");
				}
			}

			result = !super.hasErrors(context);
		}

		return result;
	}

}
