package com.spider.repository;

import com.spider.entity.SportteryAllEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SportteryAllRepository extends JpaRepository<SportteryAllEntity, Long> {

    SportteryAllEntity findByMatchCode(String matchCode);
}
