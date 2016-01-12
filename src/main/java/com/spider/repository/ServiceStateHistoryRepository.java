package com.spider.repository;

import com.spider.entity.ServiceStateHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStateHistoryRepository extends JpaRepository<ServiceStateHistoryEntity, Long> {

}
