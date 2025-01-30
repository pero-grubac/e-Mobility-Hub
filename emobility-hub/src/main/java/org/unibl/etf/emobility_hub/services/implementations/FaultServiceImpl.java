package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.base.services.impl.BaseCRUDServiceImpl;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.FaultEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;
import org.unibl.etf.emobility_hub.repositories.FaultEntityRepository;
import org.unibl.etf.emobility_hub.repositories.TransportVehicleRepository;
import org.unibl.etf.emobility_hub.services.IFaultService;

import java.time.LocalDateTime;

@Service
@Transactional
public class FaultServiceImpl
        extends BaseCRUDServiceImpl<FaultEntity, FaultRequest, FaultResponse, FaultResponse, Long>
        implements IFaultService {
    @Autowired
    private TransportVehicleRepository vehicleRepository;

    public FaultServiceImpl(ModelMapper mapper, FaultEntityRepository repository) {
        super(mapper, repository, FaultEntity.class, FaultRequest.class, FaultResponse.class, FaultResponse.class);
    }


    @Override
    public FaultResponse create(@Valid FaultRequest faultRequest) {
        TransportVehicleEntity vehicle = vehicleRepository.findById(faultRequest.getVehicleId()).orElse(null);
        if (vehicle == null)
            throw new EntityNotFoundException("Vehicle with ID " + faultRequest.getVehicleId() + " not found");
        System.out.println("1");
        vehicle.setBroken(true);
        vehicleRepository.saveAndFlush(vehicle);

        System.out.println("2");
        FaultEntity entity = new FaultEntity();
        entity.setDescription(faultRequest.getDescription());
        entity.setId(null);
        entity.setCreationDateTime(LocalDateTime.now());
        entity.setUpdateDateTime(entity.getCreationDateTime());
        entity.setVehicle(vehicle);
        getRepository().saveAndFlush(entity);
        System.out.println("3");

        FaultResponse response = new FaultResponse();
        response.setId(entity.getId());
        response.setCreationDateTime(entity.getCreationDateTime());
        response.setUpdateDateTime(entity.getUpdateDateTime());
        response.setDescription(entity.getDescription());
        response.setVehicle(getMapper().map(entity.getVehicle(), TransportVehicleResponse.class));
        System.out.println("4");

        return response;
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

}

