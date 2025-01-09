package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.base.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;

public interface IAnnouncementService extends IBaseCRUDService<AnnouncementRequest, AnnouncementResponse, AnnouncementResponse, Long> {
}
