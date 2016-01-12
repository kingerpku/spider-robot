package com.spider.repository;

import com.spider.entity.PinnacleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnacleRepository extends JpaRepository<PinnacleEntity, Long> {

    PinnacleEntity findByEventId(Long eventId);
}
