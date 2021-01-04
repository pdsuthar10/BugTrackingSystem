package com.info6250.bts.validator;

import com.info6250.bts.pojo.Project;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class ProjectFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Project project = (Project) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description", "Description cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "error.startDate", "Start Date cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "targetEndDate", "error.targetEndDate", "Target Date cannot be empty");
        if(project.getStartDate()!=null)
            if(project.getStartDate().before(new Date()))
                errors.rejectValue("startDate", "startDate.invalid", "Please select a valid date.");

        if(project.getTargetEndDate()!=null)
            if(project.getTargetEndDate().before(project.getStartDate()))
                errors.rejectValue("targetEndDate", "targetEndDate.invalid", "Please select a valid end date");


    }
}
