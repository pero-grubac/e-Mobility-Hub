package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.Announcement;

public interface AnnouncementService {
    Page<Announcement> findAll(Pageable pageable);
    Announcement create(Announcement announcement);
    Announcement update(Announcement announcement);
    void delete(Announcement announcement);
}
