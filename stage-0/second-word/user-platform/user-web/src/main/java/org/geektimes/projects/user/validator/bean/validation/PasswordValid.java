package org.geektimes.projects.user.validator.bean.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PasswordValidAnnotationValidator.class})
public @interface PasswordValid {

    boolean required() default true;

    int min() default 6;

    int max() default 32;

    String message() default "密码长度必须6-32位";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}