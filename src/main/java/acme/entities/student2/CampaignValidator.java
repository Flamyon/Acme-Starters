
package acme.entities.student2;

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
		assert context != null;

		boolean result;

		if (campaign == null)
			result = true;
		else {

			// REGLA 1: No publicada sin hitos (Esta se queda igual)
			if (campaign.getDraftMode() != null && !campaign.getDraftMode()) {
				boolean hasMilestones;
				if (campaign.getId() == 0)
					hasMilestones = false;
				else {
					Long count = this.repository.countMilestones(campaign.getId());
					hasMilestones = count != null && count > 0;
				}
				super.state(context, hasMilestones, "draftMode", "acme.validation.campaign.no-milestones.message");
			}

			// REGLA 2: Intervalo válido (SOLO comparamos inicio y fin, sin 'now')
			if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {
				// Comprobamos que el inicio sea anterior al fin
				boolean validInterval = campaign.getStartMoment().before(campaign.getEndMoment());

				super.state(context, validInterval, "endMoment", "acme.validation.campaign.invalid-interval.message");
			}

			// REGLA 3: Ticker único (La que añadimos antes)
			if (campaign.getTicker() != null && !campaign.getTicker().isBlank()) {
				Campaign existingCampaign = this.repository.findByTicker(campaign.getTicker());

				// Es válido si no existe otra con el mismo ticker, o si la que existe es la misma que editamos
				boolean isUnique = existingCampaign == null || existingCampaign.getId() == campaign.getId();

				super.state(context, isUnique, "ticker", "acme.validation.campaign.ticker.duplicate.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}

}
