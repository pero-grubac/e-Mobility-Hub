package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.Manufacturer;

public interface ManufacturerService {
    Page<Manufacturer> getAll(Pageable pageable);
    Manufacturer getById(Long id);
    Manufacturer create(Manufacturer manufacturer);
    Manufacturer update(Manufacturer manufacturer);
    void delete(Manufacturer manufacturer);
}
