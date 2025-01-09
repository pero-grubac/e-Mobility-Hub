package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.PromotionEntity;
import org.unibl.etf.emobility_hub.models.dto.request.PromotionRequest;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;
import org.unibl.etf.emobility_hub.repositories.PromotionEntityRepository;
import org.unibl.etf.emobility_hub.services.IPromotionService;

import java.time.LocalDate;

@Service
@Transactional
public class PromotionServiceImpl implements IPromotionService {

    @Autowired
    private PromotionEntityRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Page<PromotionResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(p -> mapper.map(p, PromotionResponse.class));
    }

    @Override
    public PromotionResponse create(PromotionRequest promotionRequest) {
        PromotionEntity entity = mapper.map(promotionRequest, PromotionEntity.class);
        entity.setStartDate(LocalDate.parse(promotionRequest.getStartDate()));
        entity.setEndDate(LocalDate.parse(promotionRequest.getEndDate()));
        repository.saveAndFlush(entity);
        return mapper.map(entity, PromotionResponse.class);
    }

    @Override
    public PromotionResponse update(PromotionRequest promotionRequest) {
        if (!repository.existsById(promotionRequest.getId()))
            throw new EntityNotFoundException("Promotion with ID " + promotionRequest.getId() + " not found");

        PromotionEntity entity = mapper.map(promotionRequest, PromotionEntity.class);
        entity.setStartDate(LocalDate.parse(promotionRequest.getStartDate()));
        entity.setEndDate(LocalDate.parse(promotionRequest.getEndDate()));
        repository.saveAndFlush(entity);
        return mapper.map(entity, PromotionResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Promotion with ID " + id + " not found");

        repository.deleteById(id);
        repository.flush();
    }
}
