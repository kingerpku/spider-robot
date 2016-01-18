package com.spider.dao.impl;

import com.spider.dao.W500Dao;
import com.spider.entity.W500Entity;
import com.spider.entity.W500HistoryEntity;
import com.spider.repository.W500HistoryRepository;
import com.spider.repository.W500Repository;
import com.spider.sbc.SbcUpdateManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by wsy on 2016/1/7.
 *
 * @author wsy
 */
@Repository
public class W500DaoImpl implements W500Dao {

    private static Logger logger = Logger.getLogger("500_logger");

    @Autowired
    private W500Repository w500Repository;

    @Autowired
    private W500HistoryRepository w500HistoryRepository;

    @Autowired
    private SbcUpdateManager sbcUpdateManager;

    @Override
    public void saveOrUpdate(W500Entity w500Entity) {

        W500Entity query = w500Repository.findByMatchCode(w500Entity.getMatchCode());
        if (!w500Entity.equals(query)) {
            W500Entity tmp = null;
            if (query != null) {
                tmp = new W500Entity(query);
                w500Entity.setId(query.getId());
            }
            if (w500Entity.getDurationTime().equals("0")) {
                if(!w500Entity.getHalf().equals("完")&&!w500Entity.getHalf().equals("未")) {
                    w500Entity.setDurationTime(query.getDurationTime());
                    w500Entity.setHalf(query.getHalf());
                }
            }
            w500Repository.save(w500Entity);
            logger.info("[PERSIST]-" + "save w500Entity, " + w500Entity);
            W500HistoryEntity w500HistoryEntity = new W500HistoryEntity(w500Entity);
            w500HistoryRepository.save(w500HistoryEntity);
            logger.info("[PERSIST]-" + "save w500HistoryEntity, " + w500HistoryEntity);
            if (w500Entity.needToSbc(tmp)) {
                sbcUpdateManager.updateSbcScoreAndHalf(null, String.valueOf(w500Entity.getMatchCode()));
            }
        } else {
            logger.info("[PERSIST]-" + "same as w500Entity in database");
        }
    }
}
