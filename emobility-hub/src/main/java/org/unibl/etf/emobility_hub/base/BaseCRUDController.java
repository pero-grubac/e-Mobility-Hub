package org.unibl.etf.emobility_hub.base;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.models.dto.request.BaseRequest;

@Getter
public abstract class BaseCRUDController<TRequest extends BaseRequest<ID>, TResponse, TDetailedResponse, ID> {

    private final IBaseCRUDService<TRequest, TResponse, TDetailedResponse, ID> service;

    protected BaseCRUDController(IBaseCRUDService<TRequest, TResponse, TDetailedResponse, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<TResponse>> getAll(Pageable pageable) {
        Page<TResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TDetailedResponse> getAll(@PathVariable ID id) {
        TDetailedResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TResponse> create(@RequestBody TRequest request) {
        TResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<TResponse> update(@RequestBody TRequest request) {
        TResponse response = service.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
