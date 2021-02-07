package com.javasolution.app.userservice.validators;

import com.javasolution.app.userservice.entities.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(final Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(final Object object, final Errors errors) {
        final User user = (User) object;

        if (user.getPassword() != null) {
            if (user.getPassword().length() < 4) {
                errors.rejectValue("password", "Length", "Password must be at least 4 characters");
            }

            if (!user.getPassword().equals(user.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "Match", "Password must match");
            }
        }
    }
}
