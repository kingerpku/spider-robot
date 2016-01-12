package com.spider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spider.entity.HdcOddsHistory;

public interface HdcOddsHistoryRepository extends JpaRepository<HdcOddsHistory, Long> {

}
