package com.spider.repository;

import com.spider.entity.NowgoalMatchStatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NowgoalMatchStatisticRepository extends JpaRepository<NowgoalMatchStatisticEntity, Long> {

    List<NowgoalMatchStatisticEntity> findByMatchId(Long europeId);
}
