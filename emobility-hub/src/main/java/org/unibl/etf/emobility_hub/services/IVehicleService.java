package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.response.VehicleSummaryResponse;

import java.util.List;

public interface IVehicleService {
    Page<VehicleSummaryResponse> findAll(String search, Pageable pageable);

    List<VehicleSummaryResponse> findAllWithLocation();

    VehicleSummaryResponse updateRentPrice(Long id, double rentPrice);
}
