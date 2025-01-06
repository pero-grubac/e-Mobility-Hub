package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.Manufacturer;
import org.unibl.etf.emobility_hub.models.entity.ManufacturerEntity;
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
    public Page<Manufacturer> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(m -> mapper.map(m, Manufacturer.class));
    }

    @Override
    public Manufacturer getById(Long id) {
        Optional<ManufacturerEntity> entity = repository.findById(id);
        if (entity.isEmpty())
            throw new EntityNotFoundException("Manufacturer with ID " + id + " not found");

        return mapper.map(entity.get(), Manufacturer.class);
    }

    @Override
    public Manufacturer create(@Valid Manufacturer manufacturer) {
        ManufacturerEntity entity = mapper.map(manufacturer, ManufacturerEntity.class);
        repository.saveAndFlush(entity);
        return mapper.map(entity, Manufacturer.class);
    }

    @Override
    public Manufacturer update(@Valid Manufacturer manufacturer) {
        if (!repository.existsById(manufacturer.getId()))
            throw new EntityNotFoundException("Manufacturer with ID " + manufacturer.getId() + " not found");

        ManufacturerEntity entity = mapper.map(manufacturer, ManufacturerEntity.class);
        repository.saveAndFlush(entity);
        return mapper.map(entity, Manufacturer.class);

    }

    @Override
    public void delete(Manufacturer manufacturer) {
        if (!repository.existsById(manufacturer.getId()))
            throw new EntityNotFoundException("Manufacturer with ID " + manufacturer.getId() + " not found");
        repository.deleteById(manufacturer.getId());
        repository.flush();
    }
}
