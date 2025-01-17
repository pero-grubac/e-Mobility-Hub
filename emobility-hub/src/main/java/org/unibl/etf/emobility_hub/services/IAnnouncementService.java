package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.base.services.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;

public interface IAnnouncementService
        extends IBaseCRUDService<AnnouncementRequest, AnnouncementResponse, AnnouncementResponse, Long> {
    Page<AnnouncementResponse> findAllByContent(String content, Pageable pageable);

}
