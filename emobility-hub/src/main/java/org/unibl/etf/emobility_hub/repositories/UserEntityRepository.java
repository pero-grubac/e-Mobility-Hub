package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.UserEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getByUsername(String username);

    boolean existsByUsername(String username);

    Page<UserEntity> getAllByUsernameContainingIgnoreCaseAndRoleIsNotIgnoreCase(String username, RoleEnum role, Pageable pageable);

    Page<UserEntity> findAllByRoleIsNot(RoleEnum role, Pageable pageable);
}
