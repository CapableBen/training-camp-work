package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidAnnotationValidator implements ConstraintValidator<IdValid, Long> {

    private boolean require = false;

    @Override
    public void initialize(IdValid constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (require) {
            return ValidatorUtils.idValid(id);
        } else {
            if (StringUtils.isEmpty(id.toString())) {
                return true;
            } else {
                return ValidatorUtils.idValid(id);
            }
        }
    }
}
