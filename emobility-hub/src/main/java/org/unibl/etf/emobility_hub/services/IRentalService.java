package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.response.RentalResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedRentalResponse;

public interface IRentalService {
    Page<RentalResponse> getAll(Pageable pageable);

    Page<RentalResponse> getAllByVehicleId(Long vehicleId, Pageable pageable);

    Page<RentalResponse> getAllByClientId(Long clientId, Pageable pageable);

    DetailedRentalResponse getById(Long id);
}
