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

				// Ticker uniqueness
				{
					String ticker = strategy.getTicker();
					if (ticker != null && !ticker.trim().isEmpty()) {
						Long count;
						if (strategy.getId() == 0)
							count = this.repository.countByTicker(ticker);
						else
							count = this.repository.countByTickerAndNotId(ticker, strategy.getId());

						super.state(context, count == 0, "ticker", "acme.validation.strategy.ticker-duplicated.message");
					}
				}

				// Date validations: explicit null checks before comparisons
				{
					Date now = MomentHelper.getCurrentMoment();
					Date start = strategy.getStartMoment();
					Date end = strategy.getEndMoment();

					if (start == null) {
						super.state(context, false, "startMoment", "acme.validation.strategy.start-null.message");
					}

					if (end == null) {
						super.state(context, false, "endMoment", "acme.validation.strategy.end-null.message");
					}

					if (start != null && end != null) {
						boolean startFuture = MomentHelper.isAfter(start, now);
						boolean endFuture = MomentHelper.isAfter(end, now);

						super.state(context, startFuture, "startMoment", "acme.validation.strategy.start-not-future.message");
						super.state(context, endFuture, "endMoment", "acme.validation.strategy.end-not-future.message");

						boolean validInterval = MomentHelper.isBefore(start, end);
						super.state(context, validInterval, "endMoment", "acme.validation.strategy.invalid-interval.message");
					}
				}
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}