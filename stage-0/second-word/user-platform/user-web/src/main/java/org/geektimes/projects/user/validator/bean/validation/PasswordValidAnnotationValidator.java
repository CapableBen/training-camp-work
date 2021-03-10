package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidAnnotationValidator implements ConstraintValidator<PasswordValid, String> {

    private boolean require = false;
    private int max = 32;
    private int min = 6;

    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        require = constraintAnnotation.required();
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (require) {
            return ValidatorUtils.passwordValid(password, max, min);
        } else {
            if (StringUtils.isEmpty(password)) {
                return true;
            } else {
                return ValidatorUtils.passwordValid(password, max, min);
            }
        }
    }
}