package com.spider.repository;

import com.spider.entity.PinnacleEntity;
import com.spider.entity.PinnacleHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnacleHistoryRepository extends JpaRepository<PinnacleHistoryEntity, Long> {

}
