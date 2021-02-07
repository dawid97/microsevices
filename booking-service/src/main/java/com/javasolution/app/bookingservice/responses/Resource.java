package com.javasolution.app.bookingservice.responses;

import lombok.Data;

@Data
public class Resource {

    private Long id;
    private String name;
    private String type;
    private Long userId;
}
