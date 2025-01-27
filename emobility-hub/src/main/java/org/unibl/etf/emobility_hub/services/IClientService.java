package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedClientRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;

public interface IClientService {
    Page<ClientResponse> getAll(Pageable pageable);

    ClientResponse getById(Long id);

    ClientResponse create(DetailedClientRequest request);

    ClientResponse update(DetailedClientRequest request);

    void deleteById(Long id);

    void block(Long id, boolean isBlocked);

    Page<ClientResponse> getAllByUsername(Pageable pageable, String search);
}
