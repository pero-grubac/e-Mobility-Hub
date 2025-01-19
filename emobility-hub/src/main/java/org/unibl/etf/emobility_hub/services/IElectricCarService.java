package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.base.services.IBaseVehicleCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricCarRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricCarResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricCarResponse;

public interface IElectricCarService
        extends IBaseVehicleCRUDService<ElectricCarRequest, ElectricCarResponse, DetailedElectricCarResponse> {
    Page<ElectricCarResponse> getAllByModel(String model, Pageable pageable);
}
