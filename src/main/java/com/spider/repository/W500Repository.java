package com.spider.repository;

import com.spider.entity.W500Entity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface W500Repository extends JpaRepository<W500Entity, Long> {

    W500Entity findByUniqueId(Long uniqueId);
}
