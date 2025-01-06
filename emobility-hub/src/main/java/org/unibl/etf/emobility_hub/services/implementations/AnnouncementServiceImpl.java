package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.Announcement;
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
    private ModelMapper modelMapper;


    @Override
    public Page<Announcement> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(a -> modelMapper.map(a, Announcement.class));
    }

    @Override
    public Announcement create(@Valid Announcement announcement) {
        AnnouncementEntity entity = modelMapper.map(announcement, AnnouncementEntity.class);
        entity.setCreationDate(LocalDate.now());
        entity.setUpdateDate(LocalDate.now());
        repository.saveAndFlush(entity);
        return modelMapper.map(entity, Announcement.class);
    }

    @Override
    public Announcement update(Announcement announcement) {
        if (!repository.existsById(announcement.getId()))
            throw new EntityNotFoundException("Announcement with ID " + announcement.getId() + " not found");

        AnnouncementEntity entity = modelMapper.map(announcement, AnnouncementEntity.class);
        entity.setUpdateDate(LocalDate.now());
        repository.saveAndFlush(entity);
        return modelMapper.map(entity, Announcement.class);
    }

    @Override
    public void delete(Announcement announcement) {
        if (!repository.existsById(announcement.getId()))
            throw new EntityNotFoundException("Announcement with ID " + announcement.getId() + " not found");

        repository.deleteById(announcement.getId());
        repository.flush();
    }
}
