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
import org.unibl.etf.emobility_hub.models.domain.entity.AnnouncementEntity;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;
import org.unibl.etf.emobility_hub.repositories.AnnouncementEntityRepository;
import org.unibl.etf.emobility_hub.services.IAnnouncementService;

import java.time.LocalDate;


@Service
@Transactional
public class AnnouncementServiceImpl implements IAnnouncementService {

    @Autowired
    private AnnouncementEntityRepository repository;
    @Autowired
    private ModelMapper mapper;


    @Override
    public Page<AnnouncementResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(a -> mapper.map(a, AnnouncementResponse.class));
    }

    @Override
    public AnnouncementResponse create(@Valid AnnouncementRequest announcementRequest) {
        AnnouncementEntity entity = mapper.map(announcementRequest, AnnouncementEntity.class);
        entity.setCreationDate(LocalDate.now());
        entity.setUpdateDate(entity.getCreationDate());
        repository.saveAndFlush(entity);
        return mapper.map(entity, AnnouncementResponse.class);
    }

    @Override
    public AnnouncementResponse update(@Valid AnnouncementRequest announcementRequest) {
        if (!repository.existsById(announcementRequest.getId()))
            throw new EntityNotFoundException("Announcement with ID " + announcementRequest.getId() + " not found");

        AnnouncementEntity entity = mapper.map(announcementRequest, AnnouncementEntity.class);
        entity.setUpdateDate(LocalDate.now());
        repository.saveAndFlush(entity);
        return mapper.map(entity, AnnouncementResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Announcement with ID " + id + " not found");

        repository.deleteById(id);
        repository.flush();
    }
}
