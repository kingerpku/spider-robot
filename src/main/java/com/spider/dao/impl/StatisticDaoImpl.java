package com.spider.dao.impl;

import com.spider.dao.StatisticDao;
import com.spider.entity.NowgoalMatchStatisticEntity;
import com.spider.repository.NowgoalMatchStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wsy on 2016/1/14.
 *
 * @author wsy
 */
@Repository
public class StatisticDaoImpl implements StatisticDao {

    @Autowired
    private NowgoalMatchStatisticRepository nowgoalMatchStatisticRepository;

    @Override
    public void updateMatchStatistic(long europeId, List<NowgoalMatchStatisticEntity> statisticEntities) {

        List<NowgoalMatchStatisticEntity> queryEntities;
        if ((queryEntities = nowgoalMatchStatisticRepository.findByMatchId(europeId)).size() != 0) {
            nowgoalMatchStatisticRepository.deleteByMatchId(queryEntities.get(0).getMatchId());
            nowgoalMatchStatisticRepository.save(statisticEntities);
        } else {
            nowgoalMatchStatisticRepository.save(statisticEntities);
        }
    }
}
