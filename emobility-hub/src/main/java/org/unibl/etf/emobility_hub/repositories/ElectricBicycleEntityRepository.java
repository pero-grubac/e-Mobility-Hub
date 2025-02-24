package org.unibl.etf.emobility_hub.repositories;

import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricBicycleEntity;

@Repository
public interface ElectricBicycleEntityRepository extends JpaTransportVehicleRepository<ElectricBicycleEntity> {
}
