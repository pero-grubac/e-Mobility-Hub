package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.base.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;

public interface IFaultService
        extends IBaseCRUDService<FaultRequest, FaultResponse, FaultResponse, Long> {
}
