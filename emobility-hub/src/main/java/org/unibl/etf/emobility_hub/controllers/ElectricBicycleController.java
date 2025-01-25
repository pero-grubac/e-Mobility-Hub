package org.unibl.etf.emobility_hub.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.base.controllers.BaseVehicleCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricBicycleRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricBicycleResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricBicycleResponse;
import org.unibl.etf.emobility_hub.services.IElectricBicycleService;

@RestController
@RequestMapping("/electric-bicycles")
public class ElectricBicycleController
        extends BaseVehicleCRUDController<ElectricBicycleRequest, ElectricBicycleResponse, DetailedElectricBicycleResponse> {
    public ElectricBicycleController(IElectricBicycleService service) {
        super(service);
    }

    //@GetMapping("/getByModel")
}
