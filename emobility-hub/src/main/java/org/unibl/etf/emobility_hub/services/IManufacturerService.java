package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.base.services.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedManufacturerResponse;

public interface IManufacturerService
        extends IBaseCRUDService<ManufacturerRequest,ManufacturerResponse,DetailedManufacturerResponse,Long> {
}
