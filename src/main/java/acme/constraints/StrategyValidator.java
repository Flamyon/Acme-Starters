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
		assert context != null;

		if (strategy == null)
			return true;

		boolean isValid = true;

		if (Boolean.FALSE.equals(strategy.getDraftMode())) {
			isValid &= validateTactics(strategy, context);
			isValid &= validateTicker(strategy, context);
			isValid &= validateDates(strategy, context);
		}

		return isValid;
	}

	private boolean validateTactics(final Strategy strategy, final ConstraintValidatorContext context) {
		long tacticCount = this.repository.countTacticsByStrategyId(strategy.getId());
		super.state(context, tacticCount > 0, "*", "acme.validation.strategy.no-tactics.message");
		return tacticCount > 0;
	}

	private boolean validateTicker(final Strategy strategy, final ConstraintValidatorContext context) {
		String ticker = strategy.getTicker();
		if (ticker != null && !ticker.trim().isEmpty()) {
			Long count = strategy.getId() == 0
				? this.repository.countByTicker(ticker)
				: this.repository.countByTickerAndNotId(ticker, strategy.getId());

			super.state(context, count == 0, "ticker", "acme.validation.strategy.ticker-duplicated.message");
			return count == 0;
		}
		return true;
	}

	private boolean validateDates(final Strategy strategy, final ConstraintValidatorContext context) {
		Date now = MomentHelper.getCurrentMoment();
		Date start = strategy.getStartMoment();
		Date end = strategy.getEndMoment();

		boolean isValid = true;

		if (start == null) {
			super.state(context, false, "startMoment", "acme.validation.strategy.start-null.message");
			isValid = false;
		}

		if (end == null) {
			super.state(context, false, "endMoment", "acme.validation.strategy.end-null.message");
			isValid = false;
		}

		if (start != null && end != null) {
			boolean startFuture = MomentHelper.isAfter(start, now);
			boolean endFuture = MomentHelper.isAfter(end, now);
			boolean validInterval = MomentHelper.isBefore(start, end);

			super.state(context, startFuture, "startMoment", "acme.validation.strategy.start-not-future.message");
			super.state(context, endFuture, "endMoment", "acme.validation.strategy.end-not-future.message");
			super.state(context, validInterval, "endMoment", "acme.validation.strategy.invalid-interval.message");

			isValid &= startFuture && endFuture && validInterval;
		}

		return isValid;
	}

}