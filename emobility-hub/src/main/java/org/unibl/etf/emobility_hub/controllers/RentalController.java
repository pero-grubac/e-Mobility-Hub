package org.unibl.etf.emobility_hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.models.dto.response.RentalResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedRentalResponse;
import org.unibl.etf.emobility_hub.services.IRentalService;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    @Autowired
    private IRentalService service;

    @GetMapping
    public ResponseEntity<Page<RentalResponse>> getAll(Pageable pageable) {
        Page<RentalResponse> response = service.getAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllByVehicleId/{id}")
    public ResponseEntity<Page<RentalResponse>> getAllByVehicleId(Pageable pageable, @PathVariable Long id) {
        Page<RentalResponse> response = service.getAllByVehicleId(id, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllByClientId/{id}")
    public ResponseEntity<Page<RentalResponse>> getAllByClientId(Pageable pageable, @PathVariable Long id) {
        Page<RentalResponse> response = service.getAllByClientId(id, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetailedRentalResponse> getById(@PathVariable Long id) {
        DetailedRentalResponse response = service.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
