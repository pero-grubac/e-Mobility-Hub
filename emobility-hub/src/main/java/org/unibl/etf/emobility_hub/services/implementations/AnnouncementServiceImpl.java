package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.models.entity.AnnouncementEntity;
import org.unibl.etf.emobility_hub.repositories.AnnouncementEntityRepository;
import org.unibl.etf.emobility_hub.services.AnnouncementService;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementEntityRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<AnnouncementEntity> findAll(Pageable pageable) {
        return null;
    }
}
