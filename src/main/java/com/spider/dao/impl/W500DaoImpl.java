package com.spider.dao.impl;

import com.spider.dao.W500Dao;
import com.spider.entity.W500Entity;
import com.spider.entity.W500HistoryEntity;
import com.spider.repository.W500HistoryRepository;
import com.spider.repository.W500Repository;
import com.spider.sbc.SbcUpdateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by wsy on 2016/1/7.
 *
 * @author wsy
 */
@Repository
public class W500DaoImpl implements W500Dao {

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
            //rtodo 处理half状态
            w500Repository.save(w500Entity);
            w500HistoryRepository.save(new W500HistoryEntity(w500Entity));
            if (w500Entity.needToSbc(tmp)) {
                sbcUpdateManager.updateSbcScoreAndHalf(null, String.valueOf(w500Entity.getMatchCode()));
            }
        }
    }
}
