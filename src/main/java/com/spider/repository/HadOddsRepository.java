package com.spider.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spider.entity.HadOdds;

public interface HadOddsRepository extends JpaRepository<HadOdds, Long> {

    @Query("select s from HadOdds s where WIN310_ID = :win310Id and GAMING_COMPANY = :gamingCompany")
    List<HadOdds> findByWin310IdAndGamingCompany(@Param("win310Id") Long win310Id, @Param("gamingCompany") String gamingCompany);

}
