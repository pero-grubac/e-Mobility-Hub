package org.unibl.etf.emobility_hub.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.models.dto.request.RentPriceRequest;
import org.unibl.etf.emobility_hub.models.dto.response.VehicleSummaryResponse;
import org.unibl.etf.emobility_hub.services.IVehicleService;

import java.util.List;

// All vehicle types together: fleet overview, map and rental prices.
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final IVehicleService service;

    @GetMapping
    public ResponseEntity<Page<VehicleSummaryResponse>> getAll(Pageable pageable,
                                                               @RequestParam(required = false) String search) {
        return ResponseEntity.ok(service.findAll(search, pageable));
    }

    @GetMapping("/locations")
    public ResponseEntity<List<VehicleSummaryResponse>> getLocations() {
        return ResponseEntity.ok(service.findAllWithLocation());
    }

    @PutMapping("/{id}/rent-price")
    public ResponseEntity<VehicleSummaryResponse> updateRentPrice(@PathVariable Long id,
                                                                  @Valid @RequestBody RentPriceRequest request) {
        return ResponseEntity.ok(service.updateRentPrice(id, request.getRentPrice()));
    }
}
