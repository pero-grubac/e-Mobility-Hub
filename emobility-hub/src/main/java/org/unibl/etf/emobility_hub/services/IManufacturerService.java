package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedManufacturerResponse;

public interface IManufacturerService {
    Page<ManufacturerResponse> getAll(Pageable pageable);
    DetailedManufacturerResponse getById(Long id);
    ManufacturerResponse create(ManufacturerRequest manufacturerRequest);
    ManufacturerResponse update(ManufacturerRequest manufacturerRequest);
    void delete(Long id);
}
