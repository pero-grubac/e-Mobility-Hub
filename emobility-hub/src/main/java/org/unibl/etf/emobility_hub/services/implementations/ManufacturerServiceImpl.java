package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedManufacturerResponse;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IManufacturerService;

@Service
@Transactional
public class ManufacturerServiceImpl
        extends BaseCRUDServiceImpl<ManufacturerEntity,ManufacturerRequest,ManufacturerResponse,DetailedManufacturerResponse,Long>
        implements IManufacturerService {

    @Autowired
    public ManufacturerServiceImpl(ModelMapper mapper,ManufacturerEntityRepository repository ) {
        super(mapper,repository,ManufacturerEntity.class,ManufacturerRequest.class,ManufacturerResponse.class,DetailedManufacturerResponse.class);
    }
}
