package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.FaultEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedFaultResponse;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;
import org.unibl.etf.emobility_hub.repositories.FaultEntityRepository;
import org.unibl.etf.emobility_hub.repositories.TransportVehicleRepository;
import org.unibl.etf.emobility_hub.services.IFaultService;

import java.time.LocalDateTime;

@Service
@Transactional
public class FaultServiceImpl
        extends BaseCRUDServiceImpl<FaultEntity, FaultRequest, FaultResponse, DetailedFaultResponse, Long>
        implements IFaultService {
    @Autowired
    private TransportVehicleRepository vehicleRepository;

    public FaultServiceImpl(ModelMapper mapper, FaultEntityRepository repository) {
        super(mapper, repository, FaultEntity.class, FaultRequest.class, FaultResponse.class, DetailedFaultResponse.class);
    }


    @Override
    public FaultResponse create(@Valid FaultRequest faultRequest) {
        TransportVehicleEntity vehicle = vehicleRepository.findById(faultRequest.getVehicleId()).orElse(null);
        if (vehicle == null)
            throw new EntityNotFoundException("Vehicle with ID " + faultRequest.getVehicleId() + " not found");
        vehicle.setBroken(true);
        vehicleRepository.saveAndFlush(vehicle);

        FaultEntity entity = new FaultEntity();
        entity.setDescription(faultRequest.getDescription());
        entity.setId(null);
        entity.setCreationDateTime(LocalDateTime.now());
        entity.setUpdateDateTime(entity.getCreationDateTime());
        entity.setVehicle(vehicle);
        getRepository().saveAndFlush(entity);

        return getMapper().map(entity,FaultResponse.class);
    }

    @Override
    public FaultResponse update(FaultRequest faultRequest) {
        if (!vehicleRepository.existsById(faultRequest.getVehicleId()))
            throw new EntityNotFoundException("Vehicle with ID " + faultRequest.getVehicleId() + " not found");

        if (!getRepository().existsById(faultRequest.getId()))
            throw new EntityNotFoundException("Fault with ID " + faultRequest.getId() + " not found");

        FaultEntity entity = getMapper().map(faultRequest, FaultEntity.class);
        entity.setUpdateDateTime(LocalDateTime.now());
        getRepository().saveAndFlush(entity);
        return getMapper().map(entity, FaultResponse.class);
    }

    @Override
    public Page<FaultResponse> getAllByVehicleId(Pageable pageable, Long vehicleId) {
        return ((FaultEntityRepository)getRepository()).findAllByVehicleId(vehicleId,  pageable)
                .map(fe->getMapper().map(fe, FaultResponse.class));
    }
}

