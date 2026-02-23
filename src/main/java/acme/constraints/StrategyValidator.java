package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.student3.Strategy;
import acme.entities.student3.StrategyRepository;

@Validator
public class StrategyValidator extends AbstractValidator<ValidStrategy, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private StrategyRepository repository;

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidStrategy annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Strategy strategy, final ConstraintValidatorContext context) {
		// HINT: strategy can be null
		assert context != null;

		boolean result;

		if (strategy == null)
			result = true;
		else {
			if (!strategy.getDraftMode()) {
				{
					long tacticCount;

					tacticCount = this.repository.countTacticsByStrategyId(strategy.getId());
					super.state(context, tacticCount > 0, "*", "acme.validation.strategy.no-tactics.message");
				}
				{
					Date now;
					boolean startFuture;
					boolean endFuture;

					now = MomentHelper.getCurrentMoment();
					startFuture = MomentHelper.isAfter(strategy.getStartMoment(), now);
					endFuture = MomentHelper.isAfter(strategy.getEndMoment(), now);

					super.state(context, startFuture, "startMoment", "acme.validation.strategy.start-not-future.message");
					super.state(context, endFuture, "endMoment", "acme.validation.strategy.end-not-future.message");
				}
				{
					boolean validInterval;

					validInterval = MomentHelper.isBefore(strategy.getStartMoment(), strategy.getEndMoment());
					super.state(context, validInterval, "endMoment", "acme.validation.strategy.invalid-interval.message");
				}
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}