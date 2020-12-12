package com.info6250.bts.validator;

import com.info6250.bts.pojo.Project;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProjectFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description", "Description cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "error.startDate", "Start Date cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "targetEndDate", "error.targetEndDate", "Target Date cannot be empty");
    }
}
