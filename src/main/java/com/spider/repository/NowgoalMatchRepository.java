package com.spider.repository;

import com.spider.entity.NowgoalMatchEntity;
import com.spider.entity.NowgoalMatchPlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NowgoalMatchRepository extends JpaRepository<NowgoalMatchEntity, Long> {

    List<NowgoalMatchEntity> findByEuropeId(Integer europeId);
}
