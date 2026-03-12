
package acme.entities.student1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EurValidator.class)
public @interface ValidEur {

	String message() default "Only Euros are accepted";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
