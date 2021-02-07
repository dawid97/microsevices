package com.javasolution.app.bookingservice.validators;

import com.javasolution.app.bookingservice.entities.Booking;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookingValidator implements Validator {

    @Override
    public boolean supports(final Class<?> aClass) {
        return Booking.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        final Booking booking = (Booking) object;

        if(booking.getSlotId() == null){
            errors.rejectValue("slotId", "Wrong slotId", "The slot id can not be blank");
            return;
        }
    }
}
