package com.spider.dao;

import com.spider.entity.NowgoalMatchStatisticEntity;

import java.util.List;

/**
 * Created by wsy on 2016/1/14.
 */
public interface StatisticDao {

    void updateMatchStatistic(long europeId, List<NowgoalMatchStatisticEntity> statisticEntities);
}
