package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.base.services.IBaseVehicleCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricScooterRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricScooterResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricScooterResponse;

public interface IElectricScooterService
        extends IBaseVehicleCRUDService<ElectricScooterRequest, ElectricScooterResponse, DetailedElectricScooterResponse> {
}
