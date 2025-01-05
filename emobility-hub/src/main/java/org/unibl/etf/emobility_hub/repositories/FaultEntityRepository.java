package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.entity.FaultEntity;

@Repository
public interface FaultEntityRepository extends JpaRepository<FaultEntity, Integer> {
}
