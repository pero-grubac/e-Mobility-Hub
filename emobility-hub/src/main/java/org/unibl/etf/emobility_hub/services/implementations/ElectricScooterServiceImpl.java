package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseVehicleCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricScooterEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricScooterRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricScooterResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricScooterResponse;
import org.unibl.etf.emobility_hub.repositories.ElectricScooterEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IElectricScooterService;

@Service
@Transactional
public class ElectricScooterServiceImpl
        extends BaseVehicleCRUDServiceImpl<ElectricScooterEntityRepository, ElectricScooterEntity, ElectricScooterRequest, ElectricScooterResponse, DetailedElectricScooterResponse>
        implements IElectricScooterService {
    public ElectricScooterServiceImpl(ModelMapper mapper, ElectricScooterEntityRepository repository, ManufacturerEntityRepository manufacturerEntityRepository) {
        super(mapper, repository, ElectricScooterEntity.class, ElectricScooterRequest.class, ElectricScooterResponse.class, DetailedElectricScooterResponse.class, manufacturerEntityRepository);
    }
}
