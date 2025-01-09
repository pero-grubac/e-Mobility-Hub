package org.unibl.etf.emobility_hub.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;
import org.unibl.etf.emobility_hub.services.IAnnouncementService;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    @Autowired
    private IAnnouncementService service;

    @GetMapping
    public ResponseEntity<Page<AnnouncementResponse>> getAll(Pageable pageable) {
        Page<AnnouncementResponse> responses = service.findAll(pageable);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<AnnouncementResponse> create(@RequestBody @Valid AnnouncementRequest request) {
        AnnouncementResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }
}
