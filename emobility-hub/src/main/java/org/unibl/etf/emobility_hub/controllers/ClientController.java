package org.unibl.etf.emobility_hub.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedClientRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.services.IClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private IClientService service;

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getAll(Pageable pageable) {
        Page<ClientResponse> responses = service.getAll(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Long id) {
        ClientResponse response = service.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllByUsername")
    public ResponseEntity<Page<ClientResponse>> getByUsername(Pageable pageable, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            Page<ClientResponse> responses = service.getAllByUsername(pageable, search);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        }
        return getAll(pageable);
    }

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> create(@Valid @ModelAttribute DetailedClientRequest request) {
        ClientResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClientResponse> update(@Valid @RequestBody DetailedClientRequest request) {
        ClientResponse response = service.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> block(@PathVariable Long id, @RequestBody boolean isBlocked) {
        service.block(id, isBlocked);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
