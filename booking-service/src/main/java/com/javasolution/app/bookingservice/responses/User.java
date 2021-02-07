package com.javasolution.app.bookingservice.responses;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private UserRole userRole;
    private String Ssn;
}
