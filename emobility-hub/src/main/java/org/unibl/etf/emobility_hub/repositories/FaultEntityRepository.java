package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.FaultEntity;
import org.unibl.etf.emobility_hub.models.projection.FaultCountProjection;

import java.util.List;

@Repository
public interface FaultEntityRepository extends JpaRepository<FaultEntity, Long> {
    Page<FaultEntity> findAllByVehicleId(Long vehicleId, Pageable pageable);


    // STATISTIC

    @Query("SELECT f.vehicle.id AS vehicleId, " +
            "       f.vehicle.uniqueIdentifier AS uniqueIdentifier, " +
            "       f.vehicle.model AS model, " +
            "       f.vehicle.image AS image, " +
            "       COUNT(f) AS faultCount " +
            "FROM FaultEntity f " +
            "GROUP BY f.vehicle.id, f.vehicle.uniqueIdentifier, f.vehicle.model, f.vehicle.image")
    List<FaultCountProjection> countFaultsPerVehicle();
}
