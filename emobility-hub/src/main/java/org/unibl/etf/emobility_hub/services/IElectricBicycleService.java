package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.base.services.IBaseVehicleCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricBicycleRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricBicycleResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricBicycleResponse;

public interface IElectricBicycleService
        extends IBaseVehicleCRUDService<ElectricBicycleRequest, ElectricBicycleResponse, DetailedElectricBicycleResponse> {
}
