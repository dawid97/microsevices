package com.javasolution.app.resourceservice.exceptions;

import com.javasolution.app.resourceservice.entities.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SlotsAlreadyExistException extends RuntimeException{

    final List<SlotModel> collisionSlots;

    @AllArgsConstructor
    @Data
    private class SlotModel {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    public SlotsAlreadyExistException(final String message, final List<Slot> collisionSlots) {
        super(message);

        this.collisionSlots = new ArrayList<>();

        for (Slot collisionSlot : collisionSlots) {

            this.collisionSlots.add(
                    new SlotModel(
                            collisionSlot.getStartTime(),
                            collisionSlot.getFinishTime()
                    )
            );
        }
    }
}
