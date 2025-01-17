package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.PromotionEntity;
import org.unibl.etf.emobility_hub.models.dto.request.PromotionRequest;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;
import org.unibl.etf.emobility_hub.repositories.PromotionEntityRepository;
import org.unibl.etf.emobility_hub.services.IPromotionService;

import java.time.LocalDateTime;

@Service
@Transactional
public class PromotionServiceImpl
        extends BaseCRUDServiceImpl<PromotionEntity, PromotionRequest, PromotionResponse, PromotionResponse, Long>
        implements IPromotionService {

    @Autowired
    public PromotionServiceImpl(ModelMapper mapper, PromotionEntityRepository repository) {
        super(mapper, repository, PromotionEntity.class, PromotionRequest.class, PromotionResponse.class, PromotionResponse.class);
    }

    @Override
    public PromotionResponse create(PromotionRequest promotionRequest) {
        PromotionEntity entity = getMapper().map(promotionRequest, PromotionEntity.class);
        entity.setId(null);
        entity.setStartDate(LocalDateTime.parse(promotionRequest.getStartDate()));
        entity.setEndDate(LocalDateTime.parse(promotionRequest.getEndDate()));
        getRepository().saveAndFlush(entity);
        return getMapper().map(entity, PromotionResponse.class);
    }

    @Override
    public PromotionResponse update(PromotionRequest promotionRequest) {
        PromotionEntity entity = findById(promotionRequest.getId());
        entity.setTitle(promotionRequest.getTitle());
        entity.setContent(promotionRequest.getContent());
        entity.setStartDate(LocalDateTime.parse(promotionRequest.getStartDate()));
        entity.setEndDate(LocalDateTime.parse(promotionRequest.getEndDate()));
        getRepository().saveAndFlush(entity);
        return getMapper().map(entity, PromotionResponse.class);
    }

    @Override
    public Page<PromotionResponse> findAllByContent(String content, Pageable pageable) {
        return ((PromotionEntityRepository) getRepository()).findByContentContainingIgnoreCase(content, pageable)
                .map(te -> getMapper().map(te, PromotionResponse.class));
    }
}
