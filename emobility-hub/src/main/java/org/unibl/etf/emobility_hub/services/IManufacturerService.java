package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;

public interface IManufacturerService {
    Page<ManufacturerResponse> getAll(Pageable pageable);
    ManufacturerResponse getById(Long id);
    ManufacturerResponse create(ManufacturerRequest manufacturerRequest);
    ManufacturerResponse update(ManufacturerRequest manufacturerRequest);
    void delete(ManufacturerRequest manufacturerRequest);
}
