package org.unibl.etf.emobility_hub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
