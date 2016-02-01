package com.spider.dao.impl;

import com.spider.dao.PinnaclesDao;
import com.spider.entity.PinnacleEntity;
import com.spider.entity.PinnacleHistoryEntity;
import com.spider.repository.PinnacleHistoryRepository;
import com.spider.repository.PinnacleRepository;
import com.spider.utils.LogHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Created by wsy on 2015/12/21.
 */
@Repository
public class PinnaclesDaoImpl implements PinnaclesDao {

    private Logger logger = Logger.getLogger("pinnacle_logger");

    @Autowired
    private PinnacleRepository pinnacleRepository;

    @Autowired
    private PinnacleHistoryRepository pinnacleHistoryRepository;

    @Override
    public void saveOrUpdate(PinnacleEntity pinnacleEntity) {

        PinnacleEntity query = pinnacleRepository.findByEventId(pinnacleEntity.getEventId());
        if (query != null) {
            if (pinnacleEntity.equals(query)) {
                LogHelper.persist(logger, "same as database");
                return;
            } else {
                PinnacleHistoryEntity entity = new PinnacleHistoryEntity(pinnacleEntity);
                pinnacleHistoryRepository.save(entity);
                LogHelper.persist(logger, "save pinnacle history " + entity);
                pinnacleRepository.save(pinnacleEntity);
                LogHelper.persist(logger, "save pinnacle " + pinnacleEntity);
            }
        } else {
            pinnacleRepository.save(pinnacleEntity);
            LogHelper.persist(logger, "save pinnacle " + pinnacleEntity);
        }
    }
}
