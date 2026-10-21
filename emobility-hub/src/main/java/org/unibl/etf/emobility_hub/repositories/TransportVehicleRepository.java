package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;

import java.util.List;

public interface TransportVehicleRepository extends JpaTransportVehicleRepository<TransportVehicleEntity> {
    Page<TransportVehicleEntity> findAllByModelContainingIgnoreCaseOrUniqueIdentifierContainingIgnoreCase(
            String model, String uniqueIdentifier, Pageable pageable);

    List<TransportVehicleEntity> findAllByLatitudeIsNotNullAndLongitudeIsNotNull();
}
