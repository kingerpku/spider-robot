package com.spider.dao.impl;

import java.util.concurrent.TimeUnit;

import com.spider.entity.*;
import com.spider.repository.*;
import com.spider.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spider.dao.Win310Dao;
import com.spider.utils.LogHelper;

@Service
public class Win310DaoImpl implements Win310Dao {

    private static Logger persistLogger = LogHelper.getPersistLogger();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    @Autowired
    private TCrawlerWin310Repository win310Repository;

    @Autowired
    private TCrawlerWin310HistoryRepository win310HistoryRepository;

    @Override
    @Transactional
    public boolean compareAndUpdateWin310(TCrawlerWin310 win310) {

        boolean result = false;
        try {
            TCrawlerWin310 queried = win310Repository.findByUniqueId(win310.getUniqueId());

            if (queried != null) {
                result = isUpdate(win310, result, queried);
                dealStopSaleTime(win310, queried);
                dealUniqueId(win310, queried);
                if (!queried.equals(win310)) {
                    TCrawlerWin310History history = new TCrawlerWin310History().from(win310);
                    win310HistoryRepository.save(history);
                    LogHelper.infoLog(persistLogger, null, "insert {0}", history);
                    win310.setId(queried.getId());
                    win310Repository.save(win310);
                    LogHelper.infoLog(persistLogger, null, "update {0}", win310);
                }
            } else {
                TCrawlerWin310History history = new TCrawlerWin310History().from(win310);
                win310HistoryRepository.save(history);
                LogHelper.infoLog(persistLogger, null, "insert {0}", history);
                win310Repository.save(win310);
                LogHelper.infoLog(persistLogger, null, "insert {0}", win310);
                result = true;
            }
            return result;
        } catch (Exception ex) {
            LogHelper.errorLog(errorLogger, ex, "persist win310 error {0}", win310);
            throw ex;
        }
    }

    private void dealUniqueId(TCrawlerWin310 win310, TCrawlerWin310 queried) {

        //判断开赛时间是否有所更改
        if (!queried.getUniqueId().equals(win310.getUniqueId())) {
            //判断是否在72小时之内
            if (DateUtils.diffAbsMills(win310.getStartDateTime(), queried.getStartDateTime()) < TimeUnit.HOURS.toMillis(72)
                    && queried.getUniqueId() != 0) {
                //如果在72小时之内，uniqueId保持不变
                win310.setUniqueId(queried.getUniqueId());
            }
        }
    }

    private void dealStopSaleTime(TCrawlerWin310 win310, TCrawlerWin310 queried) {

        /**
         * 由于停售时间字段和比赛进行时间字段都在一个元素里，所以要特别处理一下
         * 如果为空，就是该字段已经变为比赛进行时间了，所以停售时间要设置为之前插入的值
         */
        if (win310.getStopSaleTime() == null) {
            win310.setStopSaleTime(queried.getStopSaleTime());
        }
    }

    private boolean isUpdate(TCrawlerWin310 win310, boolean result, TCrawlerWin310 queried) {

        TCrawlerWin310 temp = new TCrawlerWin310();
        temp.setDurationTime(queried.getDurationTime());
        temp.setScore(queried.getScore());
        temp.setHomeRedCard(queried.getHomeRedCard());
        temp.setGuestRedCard(queried.getGuestRedCard());
        if (temp.isDurationTimeChanged(win310)
                || temp.isScoreChanged(win310)
                || temp.isRedCardChanged(win310)) {
            result = true;
        }
        return result;
    }
}
