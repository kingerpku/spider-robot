package com.spider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spider.entity.HiloOddsHistory;

public interface HiloOddsHistoryRepository extends JpaRepository<HiloOddsHistory, Long> {

}
