package org.unibl.etf.emobility_hub.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.BaseRequest;

public interface IBaseService<TRequest extends BaseRequest<ID>, TResponse, TDetailedResponse, ID> {
    Page<TResponse> findAll(Pageable pageable);

    TDetailedResponse getById(ID id);

    TResponse create(@Valid TRequest request);

    TResponse update(@Valid TRequest request);

    void delete(ID id);
}
