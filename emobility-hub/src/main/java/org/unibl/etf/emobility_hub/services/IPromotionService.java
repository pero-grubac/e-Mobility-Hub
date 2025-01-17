package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.base.services.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.PromotionRequest;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;

public interface IPromotionService
        extends IBaseCRUDService<PromotionRequest,PromotionResponse,PromotionResponse,Long> {

    Page<PromotionResponse> findAllByContent(String content,Pageable pageable);
}
