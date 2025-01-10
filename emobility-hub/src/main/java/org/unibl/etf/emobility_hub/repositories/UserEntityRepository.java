package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.unibl.etf.emobility_hub.models.domain.entity.UserEntity;

@NoRepositoryBean
public interface UserEntityRepository<T extends UserEntity> extends JpaRepository<T,Long> {
}
