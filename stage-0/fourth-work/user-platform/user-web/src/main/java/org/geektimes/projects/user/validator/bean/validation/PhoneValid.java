package org.geektimes.projects.user.validator.bean.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneValidAnnotationValidator.class})
public @interface PhoneValid {

    boolean required() default true;

    String message() default "采用中国大陆方式（11 位校验）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}