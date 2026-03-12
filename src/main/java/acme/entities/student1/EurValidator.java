
package acme.entities.student1;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class EurValidator extends AbstractValidator<ValidEur, Money> {

	@Override
	protected void initialise(final ValidEur annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Money value, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (value == null)
			result = true;
		else {
			String currency = value.getCurrency();
			result = currency != null && currency.trim().equalsIgnoreCase("EUR");
		}

		return result;
	}
}
