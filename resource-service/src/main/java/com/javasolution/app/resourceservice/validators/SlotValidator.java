package com.javasolution.app.resourceservice.validators;

import com.javasolution.app.resourceservice.entities.Slot;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class SlotValidator implements Validator {

    @Override
    public boolean supports(final Class<?> aClass) {
        return Slot.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        final Slot slot = (Slot) object;

        if(slot.getStartTime() == null){
            errors.rejectValue("startTime", "Wrong time", "The start time can not be blank");
            return;
        }

        if(slot.getFinishTime() == null){
            errors.rejectValue("finishTime", "Wrong time", "The finish time can not be blank");
            return;
        }

        if(slot.getResourceId() == null){
            errors.rejectValue("resourceId", "Wrong id", "The resource id can not be blank");
            return;
        }

        if(slot.getDivisor() == null){
            errors.rejectValue("divisor", "Wrong divisor", "The divisor can not be blank");
            return;
        }

        if (LocalDate.of(
                slot.getStartTime().getYear(),
                slot.getStartTime().getMonth(),
                slot.getStartTime().getDayOfMonth()
        )
                .isBefore(LocalDate.now())
        )
            errors.rejectValue("startTime", "Wrong Date", "The date cannot be in the past");

        if (slot.getFinishTime().isBefore(slot.getStartTime()))
            errors.rejectValue("finishTime", "Wrong time", "finishTime cannot be less than startTime");

        if (slot.getStartTime().equals(slot.getFinishTime()))
            errors.rejectValue("startTime", "Wrong time", "startTime and finishTime cannot be the same");
    }
}
