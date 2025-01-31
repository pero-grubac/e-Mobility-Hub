package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.RentalEntity;

@Repository
public interface RentalEntityRepository extends JpaRepository<RentalEntity, Long> {
    Page<RentalEntity> findAllByVehicle_Id(Long vehicleId, Pageable pageable);
    Page<RentalEntity> findAllByClient_Id(Long clientId, Pageable pageable);
}
