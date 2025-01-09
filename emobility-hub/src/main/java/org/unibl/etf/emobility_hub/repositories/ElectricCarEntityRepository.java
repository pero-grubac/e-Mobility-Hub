package org.unibl.etf.emobility_hub.repositories;

import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricCarEntity;

@Repository
public interface ElectricCarEntityRepository extends TransportVehicleRepository<ElectricCarEntity> {
}
