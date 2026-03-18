
package acme.constraints; // <--- Usamos el paquete que viene de develop

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.student2.Campaign;
import acme.features.student2.any.campaign.CampaignRepository;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	@Autowired
	private CampaignRepository repository;


	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
		assert context != null;
		if (campaign == null)
			return true;

		// REGLA 1: No publicada sin hitos
		if (campaign.getDraftMode() != null && !campaign.getDraftMode()) {
			boolean hasMilestones = campaign.getId() != 0 && this.repository.countMilestones(campaign.getId()) != null && this.repository.countMilestones(campaign.getId()) > 0;
			super.state(context, hasMilestones, "draftMode", "acme.validation.campaign.no-milestones.message");
		}

		// REGLA 2: Intervalo válido
		if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {
			boolean validInterval = campaign.getStartMoment().before(campaign.getEndMoment());
			super.state(context, validInterval, "endMoment", "acme.validation.campaign.invalid-interval.message");
		}

		// REGLA 3: Ticker único
		if (campaign.getTicker() != null && !campaign.getTicker().isBlank()) {
			Campaign existingCampaign = this.repository.findByTicker(campaign.getTicker());
			boolean isUnique = existingCampaign == null || existingCampaign.getId() == campaign.getId();
			super.state(context, isUnique, "ticker", "acme.validation.campaign.ticker.duplicate.message");
		}

		return !super.hasErrors(context);
	}
}
