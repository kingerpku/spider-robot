package com.spider.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spider.entity.HiloOdds;

public interface HiloOddsRepository extends JpaRepository<HiloOdds, Long> {

    @Query("select s from HiloOdds s where WIN310_ID = :win310Id and GAMING_COMPANY = :gamingCompany")
    List<HiloOdds> findByWin310IdAndGamingCompany(@Param("win310Id") Long win310Id, @Param("gamingCompany") String gamingCompany);

}
