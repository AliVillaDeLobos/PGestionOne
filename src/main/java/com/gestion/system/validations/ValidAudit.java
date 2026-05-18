package com.gestion.system.validations;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAuditOperation.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAudit {
    String message() default "ValidAudit not valid";
    Class<?> [] groups() default {};
    Class<?> [] payload() default {};
}
