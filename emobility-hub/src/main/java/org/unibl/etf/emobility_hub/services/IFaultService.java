package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;

public interface IFaultService {
    Page<FaultResponse> getAll(Pageable pageable);
    FaultResponse create(FaultRequest faultRequest);
    FaultResponse update(FaultRequest faultRequest);
    void delete(Long id);
}
