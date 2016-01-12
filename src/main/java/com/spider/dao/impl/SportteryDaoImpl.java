package com.spider.dao.impl;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.spider.entity.SportteryAllEntity;
import com.spider.entity.SportteryAllHistoryEntity;
import com.spider.repository.SportteryAllHistoryRepository;
import com.spider.repository.SportteryAllRepository;
import com.spider.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spider.entity.TCrawlerSporttery;
import com.spider.entity.TCrawlerSportteryHistory;
import com.spider.repository.TCrawlerSportteryHistoryRepository;
import com.spider.repository.TCrawlerSportteryRepository;
import com.spider.dao.SportteryDao;
import com.spider.utils.Calendars;
import com.spider.utils.LogHelper;

/**
 * @author wsy
 */
@Service
public class SportteryDaoImpl implements SportteryDao {

    private static Logger errorLogger = LogHelper.getErrorLogger();

    private static Logger persistLogger = LogHelper.getPersistLogger();

    @Autowired
    private TCrawlerSportteryRepository tCrawlerSportteryRepository;

    @Autowired
    private TCrawlerSportteryHistoryRepository tCrawlerSportteryHistoryRepository;

    @Autowired
    private SportteryAllRepository sportteryAllRepository;

    @Autowired
    private SportteryAllHistoryRepository sportteryAllHistoryRepository;

    @Override
    public void compareAndUpdateBeans(List<TCrawlerSporttery> beans) {

        for (TCrawlerSporttery bean : beans) {
            TCrawlerSporttery sporttery = tCrawlerSportteryRepository.findByCompetitionNum(bean.getCompetitionNum());
            try {
                if (sporttery != null) {
                    if (bean.equals(sporttery) && bean.getUpdateTime().after(Calendars.set(Calendar.getInstance(), 0, 0, 0).getTime())) {
                        continue;
                    }
                    tCrawlerSportteryHistoryRepository.save(new TCrawlerSportteryHistory().from(bean));
                    bean.setId(sporttery.getId());
                    tCrawlerSportteryRepository.save(bean);// 更新
                    LogHelper.infoLog(persistLogger, null, "update {0}", bean.toString());
                } else {
                    tCrawlerSportteryRepository.save(bean);// 插入
                    LogHelper.infoLog(persistLogger, null, "insert {0}", bean.toString());
                }
            } catch (Exception e) {
                LogHelper.errorLog(errorLogger, e, "save or update sporttery error {0}", bean.toString());
            }
        }
    }

    @Override
    public void compareAndUpdateSportteryAllBeans(List<SportteryAllEntity> beans) {

        for (SportteryAllEntity bean : beans) {
            SportteryAllEntity sporttery = sportteryAllRepository.findByMatchCode(bean.getMatchCode());
            try {
                if (sporttery != null) {
                    if (bean.equals(sporttery)) {
                        continue;
                    }
                    //判断是否是开赛时间有所更改
                    if (!sporttery.getUniqueId().equals(bean.getUniqueId())) {
                        //判断是否在72小时之内
                        if (DateUtils.diffAbsMills(bean.getStartDateTime(), sporttery.getStartDateTime()) < TimeUnit.HOURS.toMillis(72)
                                && sporttery.getUniqueId() != 0) {
                            //如果在72小时之内，uniqueId保持不变
                            bean.setUniqueId(sporttery.getUniqueId());
                        }
                    }
                    sportteryAllHistoryRepository.save(new SportteryAllHistoryEntity(bean));
                    bean.setId(sporttery.getId());
                    sportteryAllRepository.save(bean);// 更新
                    LogHelper.infoLog(persistLogger, null, "update {0}", bean.toString());
                } else {
                    sportteryAllHistoryRepository.save(new SportteryAllHistoryEntity(bean));
                    sportteryAllRepository.save(bean);// 插入
                    LogHelper.infoLog(persistLogger, null, "insert {0}", bean.toString());
                }
            } catch (Exception e) {
                LogHelper.errorLog(errorLogger, e, "save or update sportteryAll error {0}", bean.toString());
            }
        }
    }
}
