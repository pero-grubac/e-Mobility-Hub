package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.ClientEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ClientRequest;
import org.unibl.etf.emobility_hub.models.dto.request.LoginRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.repositories.ClientEntityRepository;
import org.unibl.etf.emobility_hub.services.IClientService;

@Service
@Transactional
public class ClientServiceImpl
        extends BaseCRUDServiceImpl<ClientEntity, ClientRequest, ClientResponse, ClientResponse, Long>
        implements IClientService {
    @Autowired
    public ClientServiceImpl(ModelMapper mapper, ClientEntityRepository repository) {
        super(mapper, repository, ClientEntity.class, ClientRequest.class, ClientResponse.class, ClientResponse.class);
    }

    @Override
    public ClientResponse create(ClientRequest request) {
        //save image
        return getMapper().map(request, ClientResponse.class);
    }

    @Override
    public ClientResponse update(ClientRequest request) {
        //image
        return getMapper().map(request, ClientResponse.class);
    }

    @Override
    public void delete(Long id) {
        // delete image
    }

    @Override
    public String login(LoginRequest request) {
        return "login succesful";
    }
}
