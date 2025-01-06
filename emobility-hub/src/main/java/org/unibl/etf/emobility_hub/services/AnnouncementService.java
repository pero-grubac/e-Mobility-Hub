package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;

public interface AnnouncementService {
    Page<AnnouncementResponse> findAll(Pageable pageable);
    AnnouncementResponse create(AnnouncementRequest announcementRequest);
    AnnouncementResponse update(AnnouncementRequest announcementRequest);
    void delete(AnnouncementRequest announcementRequest);
}
