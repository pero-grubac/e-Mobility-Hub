package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.entity.AnnouncementEntity;

public interface AnnouncementService {
    Page<AnnouncementEntity> findAll(Pageable pageable);
}
