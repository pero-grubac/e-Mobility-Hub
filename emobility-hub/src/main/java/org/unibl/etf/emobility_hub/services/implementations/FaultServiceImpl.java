package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.FaultEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub.models.dto.request.FaultRequest;
import org.unibl.etf.emobility_hub.models.dto.response.FaultResponse;
import org.unibl.etf.emobility_hub.repositories.FaultEntityRepository;
import org.unibl.etf.emobility_hub.repositories.TransportVehicleRepository;
import org.unibl.etf.emobility_hub.services.IFaultService;

import java.time.LocalDateTime;

@Service
@Transactional
public class FaultServiceImpl implements IFaultService {

    @Autowired
    private FaultEntityRepository repository;

    @Autowired
    private TransportVehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Page<FaultResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(f -> mapper.map(f, FaultResponse.class));
    }

    @Override
    public FaultResponse create(@Valid FaultRequest faultRequest) {
        TransportVehicleEntity vehicle = vehicleRepository.findById(faultRequest.getVehicleId()).orElse(null);
        if (vehicle == null)
            throw new EntityNotFoundException("Vehicle with ID " + faultRequest.getVehicleId() + " not found");
        vehicle.setBroken(true);
        vehicleRepository.saveAndFlush(vehicle);

        FaultEntity entity = mapper.map(faultRequest, FaultEntity.class);
        entity.setCreationDateTime(LocalDateTime.now());
        entity.setUpdateDateTime(entity.getCreationDateTime());
        repository.saveAndFlush(entity);
        return mapper.map(entity, FaultResponse.class);
    }

    @Override
    public FaultResponse update(FaultRequest faultRequest) {
        if (!vehicleRepository.existsById(faultRequest.getVehicleId()))
            throw new EntityNotFoundException("Vehicle with ID " + faultRequest.getVehicleId() + " not found");

        if (!repository.existsById(faultRequest.getId()))
            throw new EntityNotFoundException("Fault with ID " + faultRequest.getId() + " not found");

        FaultEntity entity = mapper.map(faultRequest, FaultEntity.class);
        entity.setUpdateDateTime(LocalDateTime.now());
        repository.saveAndFlush(entity);
        return mapper.map(entity, FaultResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Fault with ID " + id + " not found");

        repository.deleteById(id);
        repository.flush();
    }
}
