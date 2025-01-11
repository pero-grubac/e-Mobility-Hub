package org.unibl.etf.emobility_hub.repositories;

import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricScooterEntity;

@Repository
public interface ElectricScooterEntityRepository extends JpaTransportVehicleRepository<ElectricScooterEntity> {
}
