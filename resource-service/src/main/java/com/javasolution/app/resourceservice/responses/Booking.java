package com.javasolution.app.resourceservice.responses;

import lombok.Data;

@Data
public class Booking {

    private Long id;
    private Long userId;
    private Long slotId;
    private User user;
}
