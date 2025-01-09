package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.domain.entity.AnnouncementEntity;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;
import org.unibl.etf.emobility_hub.repositories.AnnouncementEntityRepository;
import org.unibl.etf.emobility_hub.services.IAnnouncementService;

import java.time.LocalDate;


@Service
@Transactional
public class AnnouncementServiceImpl extends BaseServiceImpl<AnnouncementEntity, AnnouncementRequest, AnnouncementResponse, AnnouncementResponse, Long> implements IAnnouncementService {

    @Autowired
    public AnnouncementServiceImpl(ModelMapper mapper, AnnouncementEntityRepository repository) {
        super(mapper, repository, AnnouncementEntity.class, AnnouncementRequest.class, AnnouncementResponse.class, AnnouncementResponse.class);
    }

    @Override
    public AnnouncementResponse create(@Valid AnnouncementRequest announcementRequest) {
        AnnouncementEntity entity = super.getMapper().map(announcementRequest, AnnouncementEntity.class);
        entity.setCreationDate(LocalDate.now());
        entity.setUpdateDate(entity.getCreationDate());
        super.getRepository().saveAndFlush(entity);
        return super.getMapper().map(entity, AnnouncementResponse.class);
    }

    @Override
    public AnnouncementResponse update(@Valid AnnouncementRequest announcementRequest) {
        if (!super.getRepository().existsById(announcementRequest.getId()))
            throw new EntityNotFoundException("Announcement with ID " + announcementRequest.getId() + " not found");

        AnnouncementEntity entity = super.getMapper().map(announcementRequest, AnnouncementEntity.class);
        entity.setUpdateDate(LocalDate.now());
        super.getRepository().saveAndFlush(entity);
        return super.getMapper().map(entity, AnnouncementResponse.class);
    }

}
