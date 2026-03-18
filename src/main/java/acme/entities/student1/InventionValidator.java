
package acme.entities.student1;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	@Autowired
	private InventionCostRepository repository;


	@Override
	protected void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {

			if (invention.getDraftMode() != null && !invention.getDraftMode()) {
				boolean hasParts;

				if (invention.getId() == 0)
					hasParts = false;
				else
					hasParts = this.repository.hasAnyPartByInventionId(invention.getId());

				super.state(context, hasParts, "draftMode", "acme.validation.invention.no-parts.message");
			}

			if (invention.getStartMoment() != null && invention.getEndMoment() != null) {
				boolean validInterval;

				validInterval = invention.getStartMoment().before(invention.getEndMoment());
				super.state(context, validInterval, "endMoment", "acme.validation.invention.invalid-interval.message");

				if (invention.getDraftMode() != null && !invention.getDraftMode()) {
					Date now;
					boolean futureInterval;

					now = new Date();
					futureInterval = invention.getStartMoment().after(now) && invention.getEndMoment().after(now);
					super.state(context, futureInterval, "startMoment", "acme.validation.invention.past-start-moment.message");
				}
			}

			if (invention.getTicker() != null && !invention.getTicker().isBlank()) {
				Invention existingInvention = this.repository.findByTicker(invention.getTicker());

				// Es válido si no existe otra con el mismo ticker, o si la que existe es la misma que editamos
				boolean isUnique = existingInvention == null || existingInvention.getId() == invention.getId();

				super.state(context, isUnique, "ticker", "acme.validation.campaign.ticker.duplicate.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}

}
