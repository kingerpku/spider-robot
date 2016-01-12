package com.spider.repository;

import com.spider.entity.CompanyOddsEntity;
import com.spider.entity.HadOdds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyOddsRepository extends JpaRepository<CompanyOddsEntity, Long> {

    CompanyOddsEntity findByEuropeIdAndOddsTypeAndGamingCompany(Integer europeId, Integer oddsType, String gamingCompany);
}
