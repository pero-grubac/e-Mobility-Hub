package org.unibl.etf.emobility_hub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.base.controllers.BaseVehicleCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricCarRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricCarResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricCarResponse;
import org.unibl.etf.emobility_hub.services.IElectricCarService;

@RestController
@RequestMapping("/electric-cars")
public class ElectricCarController
        extends BaseVehicleCRUDController<ElectricCarRequest, ElectricCarResponse, DetailedElectricCarResponse> {
    public ElectricCarController(IElectricCarService service) {
        super(service);
    }
}
