package com.info6250.bts.validator;

import com.info6250.bts.pojo.Issue;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class IssueValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Issue.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Issue issue = (Issue) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "error.title", "Title cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description", "Description cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "issueType", "error.issueType", "Please select an issue type");
        List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug", "Task", "Error"));
        if(!issueTypes.contains(issue.getIssueType())){
            errors.reject("error.issueType", "Invalid Value");
        }
    }
}
