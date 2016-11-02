package com.paultech.web.validators;

import com.paultech.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by paulzhang on 31/10/2016.
 */
@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        final int MINLENGTH=6;
        final int MAXLENGTH=16;
        User user = (User)o;
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < MINLENGTH || user.getPassword().length() > MAXLENGTH) {
            errors.rejectValue("password", "Length", "Password length should be between 6 and 16 letters");
        }
        if (!user.isPassword2Valid()) {
            errors.rejectValue("password2", "NotEqual", "Two passwords are not equal");
        }
    }
}
