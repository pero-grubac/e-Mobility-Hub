package org.unibl.etf.emobility_hub.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getAll")
    public ResponseEntity<Page<AnnouncementResponse>> getAll(Pageable pageable, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            Page<AnnouncementResponse> responses = ((IAnnouncementService) getService()).findAllByContent(search, pageable);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        }
        return super.getAll(pageable);
    }
}
