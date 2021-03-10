package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidAnnotationValidator implements ConstraintValidator<PhoneValid, String> {

    private boolean require = false;

    @Override
    public void initialize(PhoneValid constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (require) {
            return ValidatorUtils.phoneValid(phoneNumber);
        } else {
            if (StringUtils.isEmpty(phoneNumber)) {
                return true;
            } else {
                return ValidatorUtils.phoneValid(phoneNumber);
            }
        }
    }
}