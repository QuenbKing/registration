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
@Length(min = 4, max = 20, message = "{validation.constraint.login.length.message}")
@NotBlank(message = "{validation.constraint.login.blank.message}")
@Pattern(regexp = ".*[A-Za-z].*", message = "{validation.constraint.login.pattern.message}")
@Documented
public @interface LoginConstraint {
    String message() default "{validation.constraint.login.default.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
