package com.spider.dao.impl;

import com.spider.dao.GamingCompanyHiloDao;
import com.spider.entity.HiloOdds;
import com.spider.entity.HiloOddsHistory;
import com.spider.repository.HiloOddsHistoryRepository;
import com.spider.repository.HiloOddsRepository;
import com.spider.utils.LogHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wsy on 2015/12/9.
 *
 * @author wsy
 */
@Service
public class GamingCompanyHiloDaoImpl implements GamingCompanyHiloDao {

    private static Logger persistLogger = LogHelper.getPersistLogger();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    @Autowired
    private HiloOddsRepository hiloOddsRepository;

    @Autowired
    private HiloOddsHistoryRepository hiloOddsHistoryRepository;

    @Override
    public HiloOdds saveOrUpdateHiloOdds(List<HiloOdds> queryHiloList, List<HiloOddsHistory> hiloList, Long id, String company) {

        for (HiloOdds hiloOdds : queryHiloList) {
            hiloOdds.setWin310Id(id);
        }
        for (HiloOddsHistory history : hiloList) {
            history.setWin310Id(id);
        }
        HiloOdds result = null;
        if (!CollectionUtils.isEmpty(hiloList)) {
            HiloOdds newHilo = new HiloOdds().from(hiloList.get(0));
            if (newHilo.getOddsHigh().equals("")) {
                if (hiloList.size() > 1) {
                    newHilo = new HiloOdds().from(hiloList.get(1));
                } else {
                    LogHelper.infoLog(persistLogger, null, "invalid {0}", newHilo);
                    return result;
                }
            }
            if (CollectionUtils.isEmpty(queryHiloList)) {// no results
                hiloOddsRepository.save(newHilo);
                result = newHilo;
                LogHelper.infoLog(persistLogger, null, "insert {0}", newHilo);
                hiloOddsHistoryRepository.save(hiloList);
                LogHelper.infoLog(persistLogger, null, "insert {0}", hiloList);
            } else {
                HiloOdds hiloOdds = queryHiloList.get(0);
                if (!newHilo.equals(hiloOdds)) {
                    newHilo.setId(hiloOdds.getId());
                    dealSpecialDurationTime(newHilo, hiloOdds);
                    hiloOddsRepository.save(newHilo);
                    result = newHilo;
                    LogHelper.infoLog(persistLogger, null, "update {0}", newHilo);
                    HiloOddsHistory newHistory = new HiloOddsHistory(hiloOdds);
                    hiloOddsHistoryRepository.save(newHistory);
                    LogHelper.infoLog(persistLogger, null, "insert {0}", newHistory);
                }
            }
        } else {//本盘口未开赛或者未抓到数据，数据库记录置为空
            HiloOdds hiloOdds;
            if (CollectionUtils.isEmpty(queryHiloList)) {
                hiloOdds = new HiloOdds();
            } else {
                hiloOdds = queryHiloList.get(0);
                hiloOdds.setId(hiloOdds.getId());
                hiloOdds.setDurationTime("");
                hiloOdds.setOddsHigh("");
                hiloOdds.setOddsLow("");
                hiloOdds.setScore("");
                hiloOdds.setHandicapLine("");
                hiloOdds.setState("");
                hiloOdds.setOddsUpdateTime("");
            }
            hiloOddsRepository.save(hiloOdds);
            LogHelper.errorLog(errorLogger, null, "hilo odds is empty, win310 id is {0}", id);
        }
        return result;
    }

    private void dealSpecialDurationTime(HiloOdds newHilo, HiloOdds hiloOdds) {
        /**
         金宝博公司在中场时，时间会改为00，所以对00特殊处理，变为上一条记录的时间
         不能变为46，因为开赛之前也可能有几条记录时间为00
         */
        String durationTime = newHilo.getDurationTime();
        if ("00".equals(durationTime)) {
            newHilo.setDurationTime(hiloOdds.getDurationTime());
        } else if (StringUtils.isNotBlank(durationTime)) {//利记公司中场会变成“中场”
            if (durationTime.contains("中")) {
                newHilo.setDurationTime("46");
            }
        }
    }
}
