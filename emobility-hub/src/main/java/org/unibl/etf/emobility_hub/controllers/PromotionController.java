package org.unibl.etf.emobility_hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.base.controllers.BaseCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.PromotionRequest;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;
import org.unibl.etf.emobility_hub.services.IPromotionService;

@RestController
@RequestMapping("/promotions")
public class PromotionController
        extends BaseCRUDController<PromotionRequest, PromotionResponse, PromotionResponse, Long> {


    public PromotionController(IPromotionService service) {
        super(service);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<PromotionResponse>> getAll( Pageable pageable,@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            Page<PromotionResponse> responses = ((IPromotionService) getService()).findAllByContent(search, pageable);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        }
        return super.getAll(pageable);
    }


}
