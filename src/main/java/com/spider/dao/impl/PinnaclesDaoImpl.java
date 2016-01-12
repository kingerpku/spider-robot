package com.spider.dao.impl;

import com.spider.dao.PinnaclesDao;
import com.spider.entity.PinnacleEntity;
import com.spider.entity.PinnacleHistoryEntity;
import com.spider.repository.PinnacleHistoryRepository;
import com.spider.repository.PinnacleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.logging.Logger;

/**
 * Created by wsy on 2015/12/21.
 */
@Repository
public class PinnaclesDaoImpl implements PinnaclesDao {

    private Logger logger = Logger.getLogger("info_logger");

    @Autowired
    private PinnacleRepository pinnacleRepository;

    @Autowired
    private PinnacleHistoryRepository pinnacleHistoryRepository;

    @Override
    public void saveOrUpdate(PinnacleEntity pinnacleEntity) {

        PinnacleEntity query = pinnacleRepository.findByEventId(pinnacleEntity.getEventId());
        if (query != null) {
            if (pinnacleEntity.equals(query)) {
                return;
            } else {
                PinnacleHistoryEntity entity = new PinnacleHistoryEntity(pinnacleEntity);
                pinnacleHistoryRepository.save(entity);
                logger.info("save pinnacle history " + entity);
                pinnacleRepository.save(pinnacleEntity);
                logger.info("save pinnacle " + pinnacleEntity);
            }
        } else {
            pinnacleRepository.save(pinnacleEntity);
            logger.info("save pinnacle " + pinnacleEntity);
        }
    }
}
