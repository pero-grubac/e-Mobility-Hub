package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.base.services.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedFaultResponse;

public interface IFaultService
        extends IBaseCRUDService<FaultRequest, FaultResponse, DetailedFaultResponse, Long> {
    Page<FaultResponse> getAllByVehicleId(Pageable pageable, Long vehicleId);
}
