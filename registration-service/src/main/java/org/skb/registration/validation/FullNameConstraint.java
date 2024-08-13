package org.skb.registration.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "{validation.constraint.fullname.blank.message}")
@Pattern(regexp = "^[^\\d]+$", message = "{validation.constraint.fullname.pattern.message}")
@Documented
public @interface FullNameConstraint {
    String message() default "{validation.constraint.fullname.default.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
