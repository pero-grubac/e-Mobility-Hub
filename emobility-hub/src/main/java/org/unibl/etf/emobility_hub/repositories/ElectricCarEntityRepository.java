package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricCarEntity;

@Repository
public interface ElectricCarEntityRepository extends JpaTransportVehicleRepository<ElectricCarEntity> {
}
