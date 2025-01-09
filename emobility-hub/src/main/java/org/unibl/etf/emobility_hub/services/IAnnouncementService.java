package org.unibl.etf.emobility_hub.services;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.domain.entity.AnnouncementEntity;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;

public interface IAnnouncementService extends IBaseService<AnnouncementRequest, AnnouncementResponse, AnnouncementResponse, Long> {
}
