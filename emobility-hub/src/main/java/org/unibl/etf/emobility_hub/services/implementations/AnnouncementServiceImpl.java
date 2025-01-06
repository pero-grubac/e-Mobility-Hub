package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.entity.AnnouncementEntity;
import org.unibl.etf.emobility_hub.repositories.AnnouncementEntityRepository;
import org.unibl.etf.emobility_hub.services.AnnouncementService;

import java.time.LocalDate;


@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementEntityRepository repository;
    @Autowired
    private ModelMapper mapper;


    @Override
    public Page<AnnouncementRequest> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(a -> mapper.map(a, AnnouncementRequest.class));
    }

    @Override
    public AnnouncementRequest create(@Valid AnnouncementRequest announcementRequest) {
        AnnouncementEntity entity = mapper.map(announcementRequest, AnnouncementEntity.class);
        entity.setCreationDate(LocalDate.now());
        entity.setUpdateDate(LocalDate.now());
        repository.saveAndFlush(entity);
        return mapper.map(entity, AnnouncementRequest.class);
    }

    @Override
    public AnnouncementRequest update(@Valid AnnouncementRequest announcementRequest) {
        if (!repository.existsById(announcementRequest.getId()))
            throw new EntityNotFoundException("Announcement with ID " + announcementRequest.getId() + " not found");

        AnnouncementEntity entity = mapper.map(announcementRequest, AnnouncementEntity.class);
        entity.setUpdateDate(LocalDate.now());
        repository.saveAndFlush(entity);
        return mapper.map(entity, AnnouncementRequest.class);
    }

    @Override
    public void delete(AnnouncementRequest announcementRequest) {
        if (!repository.existsById(announcementRequest.getId()))
            throw new EntityNotFoundException("Announcement with ID " + announcementRequest.getId() + " not found");

        repository.deleteById(announcementRequest.getId());
        repository.flush();
    }
}
