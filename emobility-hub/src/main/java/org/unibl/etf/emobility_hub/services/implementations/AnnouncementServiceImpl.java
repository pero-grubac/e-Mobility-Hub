package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.domain.entity.AnnouncementEntity;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;
import org.unibl.etf.emobility_hub.repositories.AnnouncementEntityRepository;
import org.unibl.etf.emobility_hub.services.IAnnouncementService;

import java.time.LocalDateTime;


@Service
@Transactional
public class AnnouncementServiceImpl
        extends BaseCRUDServiceImpl<AnnouncementEntity, AnnouncementRequest, AnnouncementResponse, AnnouncementResponse, Long>
        implements IAnnouncementService {

    @Autowired
    public AnnouncementServiceImpl(ModelMapper mapper, AnnouncementEntityRepository repository) {
        super(mapper, repository, AnnouncementEntity.class, AnnouncementRequest.class, AnnouncementResponse.class, AnnouncementResponse.class);
    }

    @Override
    public AnnouncementResponse create(@Valid AnnouncementRequest announcementRequest) {
        AnnouncementEntity entity = getMapper().map(announcementRequest, AnnouncementEntity.class);
        entity.setId(null);
        entity.setCreationDate(LocalDateTime.now());
        entity.setUpdateDate(entity.getCreationDate());
        getRepository().saveAndFlush(entity);
        return getMapper().map(entity, AnnouncementResponse.class);
    }

    @Override
    public AnnouncementResponse update(@Valid AnnouncementRequest announcementRequest) {
        AnnouncementEntity entity = findById(announcementRequest.getId());
        entity.setTitle(announcementRequest.getTitle());
        entity.setContent(announcementRequest.getContent());
        entity.setUpdateDate(LocalDateTime.now());
        getRepository().saveAndFlush(entity);
        return getMapper().map(entity, AnnouncementResponse.class);
    }

    @Override
    public Page<AnnouncementResponse> findAllByContent(String content, Pageable pageable) {
        return ((AnnouncementEntityRepository) getRepository()).findByContentContainingIgnoreCase(content, pageable)
                .map(te -> getMapper().map(te, AnnouncementResponse.class));    }
}
