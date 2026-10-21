package org.unibl.etf.emobility_hub.services.implementations;

import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.RentalEntity;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.models.dto.response.RentalResponse;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedRentalResponse;
import org.unibl.etf.emobility_hub.repositories.RentalEntityRepository;
import org.unibl.etf.emobility_hub.services.IRentalService;

@Service
@Transactional
@RequiredArgsConstructor
public class IRentalServiceImpl implements IRentalService {

    private final ModelMapper modelMapper;

    private final RentalEntityRepository repository;


    @Override
    public Page<RentalResponse> getAll(Pageable pageable, String search) {
        Page<RentalEntity> page = search == null || search.isBlank()
                ? repository.findAll(pageable)
                : repository.findAllByClient_UsernameContainingIgnoreCaseOrVehicle_UniqueIdentifierContainingIgnoreCase(search, search, pageable);
        return page.map(entity -> modelMapper.map(entity, RentalResponse.class));
    }

    @Override
    public Page<RentalResponse> getAllByVehicleId(Long vehicleId, Pageable pageable, String search) {
        return repository.findAllByVehicle_IdAndClient_UsernameContainingIgnoreCase(vehicleId, search == null ? "" : search, pageable)
                .map(entity -> modelMapper.map(entity, RentalResponse.class));
    }

    @Override
    public DetailedRentalResponse getById(Long id) {
        RentalEntity entity = repository.findById(id).orElse(null);
        if (entity == null)
            throw new EntityNotFoundException("Rental with id " + id + " not found");
        DetailedRentalResponse response = modelMapper.map(entity, DetailedRentalResponse.class);
        response.setClient(modelMapper.map(entity.getClient(), ClientResponse.class));
        response.setVehicle(modelMapper.map(entity.getVehicle(), TransportVehicleResponse.class));
        return response;
    }
}
