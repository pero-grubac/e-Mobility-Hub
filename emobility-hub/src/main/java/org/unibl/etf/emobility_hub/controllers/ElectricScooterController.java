package org.unibl.etf.emobility_hub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.base.controllers.BaseVehicleCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricScooterRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricScooterResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricScooterResponse;
import org.unibl.etf.emobility_hub.services.IElectricScooterService;

@RestController
@RequestMapping("/electric-scooters")
public class ElectricScooterController
        extends BaseVehicleCRUDController<ElectricScooterRequest, ElectricScooterResponse, DetailedElectricScooterResponse> {

    public ElectricScooterController(IElectricScooterService service) {
        super(service);
    }
}
