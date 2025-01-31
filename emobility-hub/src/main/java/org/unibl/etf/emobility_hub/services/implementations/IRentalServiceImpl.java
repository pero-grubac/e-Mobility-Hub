package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.models.dto.response.RentalResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedRentalResponse;
import org.unibl.etf.emobility_hub.repositories.RentalEntityRepository;
import org.unibl.etf.emobility_hub.services.IRentalService;

@Service
@Transactional
public class IRentalServiceImpl implements IRentalService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RentalEntityRepository repository;


    @Override
    public Page<RentalResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<RentalResponse> getAllByVehicleId(Long vehicleId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<RentalResponse> getAllByClientId(Long clientId, Pageable pageable) {
        return null;
    }

    @Override
    public DetailedRentalResponse getById(Long id) {
        return null;
    }
}
