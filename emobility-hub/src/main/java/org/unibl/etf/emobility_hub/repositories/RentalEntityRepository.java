package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.RentalEntity;
import org.unibl.etf.emobility_hub.models.projection.DailyRevenueProjection;
import org.unibl.etf.emobility_hub.models.projection.RevenueByVehicleTypeProjection;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalEntityRepository extends JpaRepository<RentalEntity, Long> {
    Page<RentalEntity> findAllByVehicle_Id(Long vehicleId, Pageable pageable);
    Page<RentalEntity> findAllByClient_Id(Long clientId, Pageable pageable);




    // STATISTIC

    @Query("SELECT FUNCTION('DATE', r.rentalStart) AS rentalDay, SUM(r.price) AS totalRevenue " +
            "FROM RentalEntity r " +
            "WHERE r.rentalStart BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('DATE', r.rentalStart)")
    List<DailyRevenueProjection> findDailyRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);



    @Query("SELECT " +
            "  CASE " +
            "    WHEN TYPE(r.vehicle) = ElectricCarEntity THEN 'Electric Car' " +
            "    WHEN TYPE(r.vehicle) = ElectricBicycleEntity THEN 'Electric Bicycle' " +
            "    WHEN TYPE(r.vehicle) = ElectricScooterEntity THEN 'Electric Scooter' " +
            "    ELSE 'Other' " +
            "  END as vehicleType, " +
            "  SUM(r.price) as totalRevenue " +
            "FROM RentalEntity r " +
            "GROUP BY " +
            "  CASE " +
            "    WHEN TYPE(r.vehicle) = ElectricCarEntity THEN 'Electric Car' " +
            "    WHEN TYPE(r.vehicle) = ElectricBicycleEntity THEN 'Electric Bicycle' " +
            "    WHEN TYPE(r.vehicle) = ElectricScooterEntity THEN 'Electric Scooter' " +
            "    ELSE 'Other' " +
            "  END")
    List<RevenueByVehicleTypeProjection> findRevenueByVehicleType();
}
