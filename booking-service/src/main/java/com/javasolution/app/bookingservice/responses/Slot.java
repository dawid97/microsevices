package com.javasolution.app.bookingservice.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Slot {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Boolean isBooked;
    private Long resourceId;
}
