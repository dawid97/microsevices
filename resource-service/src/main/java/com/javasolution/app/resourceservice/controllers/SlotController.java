package com.javasolution.app.resourceservice.controllers;

import com.javasolution.app.resourceservice.entities.Slot;
import com.javasolution.app.resourceservice.services.MapValidationErrorService;
import com.javasolution.app.resourceservice.services.SlotService;
import com.javasolution.app.resourceservice.validators.SlotValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class SlotController {

    private final SlotService slotService;
    private final SlotValidator slotValidator;
    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("/slots")
    public ResponseEntity<?> addSlot(@RequestBody final Slot slot, final Principal principal, final BindingResult result){

        slotValidator.validate(slot, result);

        final ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        return new ResponseEntity<>(slotService.addSlot(slot, principal), HttpStatus.CREATED);
    }

    @GetMapping("/slots")
    public ResponseEntity<?> getSlots(){
        return new ResponseEntity<>(slotService.getSlots(),HttpStatus.OK);
    }

    @GetMapping("/slots/{id}")
    public ResponseEntity<?> getSlot(@PathVariable final long id){
        return new ResponseEntity<>(slotService.getSlot(id), HttpStatus.OK);
    }

    @GetMapping("/resources/{resourceId}/slots")
    public ResponseEntity<?> getResourceSlots(@PathVariable final long resourceId){
        return new ResponseEntity<>(slotService.getResourceSlots(resourceId), HttpStatus.OK);
    }

    @PostMapping("/slots/{id}")
    public ResponseEntity<?> updateSlot(@RequestParam final boolean isBooked, @PathVariable final long id){
        return new ResponseEntity<>(slotService.updateSlot(isBooked, id), HttpStatus.OK);
    }

    @DeleteMapping("/slots/{id}")
    public ResponseEntity<?> deleteSlot(@PathVariable final long id, final Principal principal){
        slotService.deleteSlot(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
