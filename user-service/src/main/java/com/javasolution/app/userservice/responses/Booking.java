package com.javasolution.app.userservice.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {

    private Long id;
    private LocalDateTime createAt;
    private Long userId;
    private Long slotId;
    private LocalDateTime updateAt;
}
