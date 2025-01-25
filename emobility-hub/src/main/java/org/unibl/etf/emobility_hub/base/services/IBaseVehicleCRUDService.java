package org.unibl.etf.emobility_hub.base.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.TransportVehicleRequest;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.IContainManufacturer;

public interface IBaseVehicleCRUDService<TRequest extends TransportVehicleRequest, TResponse, TDetailedResponse extends IContainManufacturer> {
    Page<TResponse> findAll(Pageable pageable);

    Page<TResponse> getAllByModel(String model, Pageable pageable);

    TDetailedResponse getById(Long id);

    TResponse create(@Valid TRequest request);

    TResponse update(@Valid TRequest request);

    void delete(Long id);

    Page<TResponse> findAllBroken(Pageable pageable);

    Boolean rent(Long id);

    void fix(Long id);

    void brake(Long id);
}
