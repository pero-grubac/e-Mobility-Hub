package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;

public interface ManufacturerService {
    Page<ManufacturerRequest> getAll(Pageable pageable);
    ManufacturerRequest getById(Long id);
    ManufacturerRequest create(ManufacturerRequest manufacturerRequest);
    ManufacturerRequest update(ManufacturerRequest manufacturerRequest);
    void delete(ManufacturerRequest manufacturerRequest);
}
