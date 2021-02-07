package com.javasolution.app.resourceservice.requests;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
