package org.unibl.etf.emobility_hub.controllers;

import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.base.controllers.BaseCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;
import org.unibl.etf.emobility_hub.services.IAnnouncementService;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController
        extends BaseCRUDController<AnnouncementRequest,AnnouncementResponse,AnnouncementResponse,Long> {

    public AnnouncementController(IAnnouncementService service) {
        super(service);
    }
}
