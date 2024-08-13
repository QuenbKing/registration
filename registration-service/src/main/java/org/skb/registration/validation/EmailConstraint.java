package org.skb.registration.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "{validation.constraint.email.blank.message}")
@Email(message = "{validation.constraint.email.pattern.message}")
@Documented
public @interface EmailConstraint {
    String message() default "{validation.constraint.email.default.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
