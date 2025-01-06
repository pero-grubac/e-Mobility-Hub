package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;

public interface AnnouncementService {
    Page<AnnouncementRequest> findAll(Pageable pageable);
    AnnouncementRequest create(AnnouncementRequest announcementRequest);
    AnnouncementRequest update(AnnouncementRequest announcementRequest);
    void delete(AnnouncementRequest announcementRequest);
}
