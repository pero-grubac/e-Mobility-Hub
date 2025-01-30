package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.FaultEntity;

@Repository
public interface FaultEntityRepository extends JpaRepository<FaultEntity, Long> {
    Page<FaultEntity> findAllByVehicleId(Long vehicleId, Pageable pageable);
}
