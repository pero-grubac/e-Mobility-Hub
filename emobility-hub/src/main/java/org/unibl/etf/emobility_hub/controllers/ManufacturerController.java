package org.unibl.etf.emobility_hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.base.controllers.BaseCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.dto.response.BaseManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedManufacturerResponse;
import org.unibl.etf.emobility_hub.services.IManufacturerService;

import java.util.List;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController
        extends BaseCRUDController<ManufacturerRequest, ManufacturerResponse, DetailedManufacturerResponse, Long> {
    @Autowired
    public ManufacturerController(IManufacturerService service) {
        super(service);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BaseManufacturerResponse> > getAll(){
        List<BaseManufacturerResponse> response = ((IManufacturerService)getService()).getAll();
        return ResponseEntity.ok(response);
    }
}
