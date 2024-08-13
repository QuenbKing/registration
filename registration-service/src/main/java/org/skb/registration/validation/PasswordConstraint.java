package org.skb.registration.validation;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Length(min = 6, max = 18, message = "{validation.constraint.password.length.message}")
@NotBlank(message = "{validation.constraint.password.blank.message}")
@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "{validation.constraint.password.pattern.message}")
@Documented
public @interface PasswordConstraint {
    String message() default "{validation.constraint.password.default.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
