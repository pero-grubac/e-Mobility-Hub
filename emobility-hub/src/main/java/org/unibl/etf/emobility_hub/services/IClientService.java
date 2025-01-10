package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.base.services.IBaseCRUDService;
import org.unibl.etf.emobility_hub.models.dto.request.ClientRequest;
import org.unibl.etf.emobility_hub.models.dto.request.LoginRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;

public interface IClientService extends IBaseCRUDService<ClientRequest, ClientResponse, ClientResponse, Long> {
    String login(LoginRequest request);
}
