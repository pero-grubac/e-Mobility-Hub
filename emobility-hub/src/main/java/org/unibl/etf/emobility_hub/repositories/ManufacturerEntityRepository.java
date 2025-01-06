package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;

@Repository
public interface ManufacturerEntityRepository extends JpaRepository<ManufacturerEntity, Long> {
}
