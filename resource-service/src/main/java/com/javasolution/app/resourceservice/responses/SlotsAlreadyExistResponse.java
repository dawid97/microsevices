package com.javasolution.app.resourceservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SlotsAlreadyExistResponse {

    private String slot;

    private List<Object> slots;
}
