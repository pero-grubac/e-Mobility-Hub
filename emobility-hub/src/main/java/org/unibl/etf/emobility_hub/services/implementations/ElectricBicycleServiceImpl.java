package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseVehicleCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricBicycleEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricBicycleRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricBicycleResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricBicycleResponse;
import org.unibl.etf.emobility_hub.repositories.ElectricBicycleEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IElectricBicycleService;

@Service
@Transactional
public class ElectricBicycleServiceImpl
        extends BaseVehicleCRUDServiceImpl<ElectricBicycleEntityRepository, ElectricBicycleEntity, ElectricBicycleRequest, ElectricBicycleResponse, DetailedElectricBicycleResponse>
        implements IElectricBicycleService {

    @Autowired
    public ElectricBicycleServiceImpl(ModelMapper mapper, ElectricBicycleEntityRepository repository, ManufacturerEntityRepository manufacturerEntityRepository) {
        super(mapper, repository, ElectricBicycleEntity.class, ElectricBicycleRequest.class, ElectricBicycleResponse.class, DetailedElectricBicycleResponse.class, manufacturerEntityRepository);
    }
}
