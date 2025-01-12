package org.unibl.etf.emobility_hub.base.controllers;

import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.base.services.IBaseVehicleCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.TransportVehicleRequest;

@Getter
public abstract class BaseVehicleCRUDController<TRequest extends TransportVehicleRequest, TResponse, TDetailedResponse> {
    private final IBaseVehicleCRUDService<TRequest, TResponse, TDetailedResponse> service;

    protected BaseVehicleCRUDController(IBaseVehicleCRUDService<TRequest, TResponse, TDetailedResponse> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<TResponse>> findAll(Pageable pageable) {
        Page<TResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TDetailedResponse> getById(@PathVariable Long id) {
        TDetailedResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TResponse> create(@Valid @ModelAttribute TRequest request) {
        TResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<TResponse> update(@Valid @RequestBody TRequest request) {
        TResponse response = service.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/broken")
    public ResponseEntity<Page<TResponse>> findAllBroken(Pageable pageable) {
        Page<TResponse> response = service.findAllBroken(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/rent/{id}")
    public ResponseEntity<?> rent(@PathVariable Long id) {
        Boolean result = service.rent(id);
        if (result)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/fix/{id}")
    public ResponseEntity<?> fix(@PathVariable Long id) {
        service.fix(id);
        return ResponseEntity.ok().build();
    }

    // operater
    @PutMapping("/brake/{id}")
    public ResponseEntity<?> brake(@PathVariable Long id) {
        service.brake(id);
        return ResponseEntity.ok().build();
    }
    // TODO create from csv
}
