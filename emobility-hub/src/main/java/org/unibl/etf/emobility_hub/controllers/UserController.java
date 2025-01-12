package org.unibl.etf.emobility_hub.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.models.dto.request.UserRequest;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedUserRequest;
import org.unibl.etf.emobility_hub.models.dto.response.UserResponse;
import org.unibl.etf.emobility_hub.services.IUserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService service;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(Pageable pageable) {
        Page<UserResponse> responses = service.getAll(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse response = service.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody DetailedUserRequest request) {
        UserResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@Valid @RequestBody DetailedUserRequest request) {
        UserResponse response = service.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> changeRole(@PathVariable Long id, @RequestBody String role) {
        UserResponse response = service.changeRole(id, role);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
