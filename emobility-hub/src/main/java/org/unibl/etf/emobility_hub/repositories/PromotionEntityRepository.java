package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.PromotionEntity;

@Repository
public interface PromotionEntityRepository extends JpaRepository<PromotionEntity, Long> {
    Page<PromotionEntity> findByContentContainingIgnoreCase(String content, Pageable pageable);

}
