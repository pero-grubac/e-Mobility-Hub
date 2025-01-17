package org.unibl.etf.emobility_hub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.emobility_hub.models.domain.entity.AnnouncementEntity;

@Repository
public interface AnnouncementEntityRepository extends JpaRepository<AnnouncementEntity, Long> {
    Page<AnnouncementEntity> findByContentContainingIgnoreCase(String content, Pageable pageable);

}
