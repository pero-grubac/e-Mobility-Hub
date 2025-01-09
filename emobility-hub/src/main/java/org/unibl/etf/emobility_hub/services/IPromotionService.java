package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.PromotionRequest;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;

public interface IPromotionService {
    Page<PromotionResponse> getAll(Pageable pageable);
    PromotionResponse create(PromotionRequest promotionRequest);
    PromotionResponse update(PromotionRequest promotionRequest);
    void delete(Long id);
}
