package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.dto.response.BaseManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedManufacturerResponse;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IManufacturerService;

import java.util.List;

@Service
@Transactional
public class ManufacturerServiceImpl
        extends BaseCRUDServiceImpl<ManufacturerEntity, ManufacturerRequest, ManufacturerResponse, DetailedManufacturerResponse, Long>
        implements IManufacturerService {

    @Autowired
    public ManufacturerServiceImpl(ModelMapper mapper, ManufacturerEntityRepository repository) {
        super(mapper, repository, ManufacturerEntity.class, ManufacturerRequest.class, ManufacturerResponse.class, DetailedManufacturerResponse.class);
    }

    @Override
    public List<BaseManufacturerResponse> getAll() {
        return getRepository().findAll().stream().map(m -> getMapper().map(m, BaseManufacturerResponse.class)).toList();
    }

    @Override
    public Page<ManufacturerResponse> getAllByName(Pageable pageable, String name) {
        return ((ManufacturerEntityRepository) getRepository()).findAllByNameContainingIgnoreCase(name, pageable)
                .map(me -> getMapper().map(me, ManufacturerResponse.class));
    }
}
