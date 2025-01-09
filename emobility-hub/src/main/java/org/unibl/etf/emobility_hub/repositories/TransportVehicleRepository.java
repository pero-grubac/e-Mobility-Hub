package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;

public interface TransportVehicleRepository extends JpaRepository<TransportVehicleEntity, Long> {
}
