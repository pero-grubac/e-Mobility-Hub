package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;
import org.unibl.etf.emobility_hub.models.domain.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.ManufacturerService;

import java.util.Optional;

@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerEntityRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Page<ManufacturerResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(m -> mapper.map(m, ManufacturerResponse.class));
    }

    @Override
    public ManufacturerResponse getById(Long id) {
        Optional<ManufacturerEntity> entity = repository.findById(id);
        if (entity.isEmpty())
            throw new EntityNotFoundException("Manufacturer with ID " + id + " not found");

        return mapper.map(entity.get(), ManufacturerResponse.class);
    }

    @Override
    public ManufacturerResponse create(@Valid ManufacturerRequest manufacturerRequest) {
        ManufacturerEntity entity = mapper.map(manufacturerRequest, ManufacturerEntity.class);
        repository.saveAndFlush(entity);
        return mapper.map(entity, ManufacturerResponse.class);
    }

    @Override
    public ManufacturerResponse update(@Valid ManufacturerRequest manufacturerRequest) {
        if (!repository.existsById(manufacturerRequest.getId()))
            throw new EntityNotFoundException("Manufacturer with ID " + manufacturerRequest.getId() + " not found");

        ManufacturerEntity entity = mapper.map(manufacturerRequest, ManufacturerEntity.class);
        repository.saveAndFlush(entity);
        return mapper.map(entity, ManufacturerResponse.class);

    }

    @Override
    public void delete(ManufacturerRequest manufacturerRequest) {
        if (!repository.existsById(manufacturerRequest.getId()))
            throw new EntityNotFoundException("Manufacturer with ID " + manufacturerRequest.getId() + " not found");
        repository.deleteById(manufacturerRequest.getId());
        repository.flush();
    }
}
