package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;

@NoRepositoryBean
public interface JpaTransportVehicleRepository<T extends TransportVehicleEntity>
        extends JpaRepository<T, Long> {
    Page<T> findAllByIsBroken(Pageable pageable,Boolean isBroken);
}
