package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.ElectricCarEntity;

@Repository
public interface ElectricCarEntityRepository extends CrudRepository<ElectricCarEntity, Long> {
}
