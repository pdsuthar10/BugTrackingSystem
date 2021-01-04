package com.info6250.bts.validator;

import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Name cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required", "Username cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Password cannot be empty");

        if(!(user.getUsername() == null || user.getUsername().trim().equals(""))){
            User found = userDAO.findUserByUsername(user.getUsername());
            if(found != null){
                errors.rejectValue("username", "username.invalid", "User with such a username exists! Please choose a different username");
            }else{
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
                if(!(pattern.matcher(user.getUsername()).matches())){
                    errors.rejectValue("username", "username.invalid", "Invalid format for the email! Example: test@xyz.com");
                }
            }
        }

        String password = user.getPassword();
        if(password == null || password.trim().equals("")) return;

        if(6 > password.length() || 20 < password.length()) {
            errors.rejectValue("password", "password.length", "Password should have length between 6 and 20 characters");
            return;
        }

        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,20}");
        if(!(pattern.matcher(password).matches())) {
            errors.rejectValue("password", "password.invalid", "Password must contain atleast 1 digit, lowercase letter, uppercase letter, special character and no whitespace");
        }

    }
}
