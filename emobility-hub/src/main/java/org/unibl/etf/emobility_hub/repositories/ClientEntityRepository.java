package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.ClientEntity;

@Repository
public interface ClientEntityRepository extends JpaUserEntityRepository<ClientEntity> {
    Boolean existsByUsername(String username);

    Page<ClientEntity> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
