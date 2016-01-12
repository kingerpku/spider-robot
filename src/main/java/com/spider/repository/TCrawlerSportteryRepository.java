package com.spider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spider.entity.TCrawlerSporttery;

public interface TCrawlerSportteryRepository extends JpaRepository<TCrawlerSporttery, Long>, JpaSpecificationExecutor<TCrawlerSporttery> {

    TCrawlerSporttery findByCompetitionNum(String competitionNum);
}
