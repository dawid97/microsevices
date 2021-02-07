package com.javasolution.app.resourceservice.validators;

import com.javasolution.app.resourceservice.entities.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResourceValidator implements Validator {

    @Override
    public boolean supports(final Class<?> aClass) {
        return Resource.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        final Resource resource = (Resource) object;

        if(resource.getName() == null){
            errors.rejectValue("name", "Wrong name", "The name can not be blank");
            return;
        }

        if(resource.getType() == null){
            errors.rejectValue("type", "Wrong type", "The type can not be blank");
            return;
        }
    }
}
