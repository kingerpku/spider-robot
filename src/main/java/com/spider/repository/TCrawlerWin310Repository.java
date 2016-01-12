package com.spider.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spider.entity.TCrawlerWin310;

public interface TCrawlerWin310Repository extends JpaRepository<TCrawlerWin310, Long>, JpaSpecificationExecutor<TCrawlerWin310> {

    @Query("select s from TCrawlerWin310 s where s.competitionNum = :competitionNum")
    TCrawlerWin310 findByCompetitionNum(@Param("competitionNum") String competitionNum);

    @Query("select s from TCrawlerWin310 s where s.durationTime != '完场' and s.durationTime != '取消' and s.durationTime !='腰斩'")
    List<TCrawlerWin310> findOnSaleMatches();

    @Query("select s from TCrawlerWin310 s where s.durationTime != '完场' and s.durationTime != '取消' and s.durationTime !='腰斩'")
    List<TCrawlerWin310> findNotCompletedMatches();
}
