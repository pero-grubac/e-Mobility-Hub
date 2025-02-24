package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.base.services.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.dto.response.BaseManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedManufacturerResponse;

import java.util.List;

public interface IManufacturerService
        extends IBaseCRUDService<ManufacturerRequest, ManufacturerResponse, DetailedManufacturerResponse, Long> {

    List<BaseManufacturerResponse> getAll();

    Page<ManufacturerResponse> getAllByName(Pageable pageable, String name);
    
}
