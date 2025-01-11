package org.unibl.etf.emobility_hub.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.base.controllers.BaseCRUDController;
import org.unibl.etf.emobility_hub.models.dto.request.ClientRequest;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedClientRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.services.IClientService;

@RestController
@RequestMapping("/clients")
public class ClientController
        extends BaseCRUDController<ClientRequest, ClientResponse, ClientResponse, Long> {
    public ClientController(IClientService service) {
        super(service);
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<ClientResponse> create(@RequestBody ClientRequest request) {
        return super.create(request);
    }
    // login//register
}
