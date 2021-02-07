package com.javasolution.app.resourceservice.controllers;

import com.javasolution.app.resourceservice.entities.Resource;
import com.javasolution.app.resourceservice.services.MapValidationErrorService;
import com.javasolution.app.resourceservice.services.ResourceService;
import com.javasolution.app.resourceservice.validators.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final MapValidationErrorService mapValidationErrorService;
    private final ResourceValidator resourceValidator;

    @PostMapping
    public ResponseEntity<?> addResource(@RequestBody final Resource resource, final Principal principal, final BindingResult result){
        resourceValidator.validate(resource, result);

        final ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        return new ResponseEntity<>(resourceService.addResource(resource, principal), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getResources(final Principal principal){
        return new ResponseEntity<>(resourceService.getResources(principal), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResource(@PathVariable final long id){
        return new ResponseEntity<>(resourceService.getResource(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable final long id, final Principal principal){
        resourceService.deleteResource(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
