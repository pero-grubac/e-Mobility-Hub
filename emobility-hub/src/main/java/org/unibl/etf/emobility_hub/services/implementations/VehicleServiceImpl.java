package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricBicycleEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricCarEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub.models.dto.response.VehicleSummaryResponse;
import org.unibl.etf.emobility_hub.repositories.TransportVehicleRepository;
import org.unibl.etf.emobility_hub.services.IVehicleService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements IVehicleService {
    private final TransportVehicleRepository repository;

    @Override
    public Page<VehicleSummaryResponse> findAll(String search, Pageable pageable) {
        Page<TransportVehicleEntity> page = search == null || search.isBlank()
                ? repository.findAll(pageable)
                : repository.findAllByModelContainingIgnoreCaseOrUniqueIdentifierContainingIgnoreCase(search, search, pageable);
        return page.map(VehicleServiceImpl::toSummary);
    }

    @Override
    public List<VehicleSummaryResponse> findAllWithLocation() {
        return repository.findAllByLatitudeIsNotNullAndLongitudeIsNotNull().stream()
                .map(VehicleServiceImpl::toSummary)
                .toList();
    }

    @Override
    public VehicleSummaryResponse updateRentPrice(Long id, double rentPrice) {
        TransportVehicleEntity vehicle = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vehicle with ID " + id + " not found"));
        vehicle.setRentPrice(rentPrice);
        repository.saveAndFlush(vehicle);
        return toSummary(vehicle);
    }

    static String typeOf(TransportVehicleEntity vehicle) {
        if (vehicle instanceof ElectricCarEntity) {
            return "CAR";
        }
        if (vehicle instanceof ElectricBicycleEntity) {
            return "BICYCLE";
        }
        return "SCOOTER";
    }

    static VehicleSummaryResponse toSummary(TransportVehicleEntity v) {
        return new VehicleSummaryResponse(v.getId(), typeOf(v), v.getUniqueIdentifier(), v.getModel(),
                v.getManufacturer().getName(), v.getPurchasePrice(), v.getRentPrice(), v.isBroken(), v.isRented(),
                v.getLatitude(), v.getLongitude(), v.getImage());
    }
}
