package org.unibl.etf.emobility_hub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.base.controllers.BaseCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedFaultResponse;
import org.unibl.etf.emobility_hub.services.IFaultService;

@RestController
@RequestMapping("/faults")
public class FaultController
        extends BaseCRUDController<FaultRequest, FaultResponse, DetailedFaultResponse, Long> {

    public FaultController(IFaultService service) {
        super(service);
    }
    // get by vehicle id
}
