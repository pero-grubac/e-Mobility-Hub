package org.unibl.etf.emobility_hub.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/getByModel")
    public ResponseEntity<Page<ElectricCarResponse>> getByModel(Pageable pageable, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            Page<ElectricCarResponse> responses = ((IElectricCarService) super.getService()).getAllByModel(search, pageable);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        }
        return super.findAll(pageable);
    }

}
