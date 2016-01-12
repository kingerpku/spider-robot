package com.spider.dao.impl;

import com.spider.dao.GamingCompanyHadDao;
import com.spider.entity.HadOdds;
import com.spider.entity.HadOddsHistory;
import com.spider.repository.HadOddsHistoryRepository;
import com.spider.repository.HadOddsRepository;
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
public class GamingCompanyHadDaoImpl implements GamingCompanyHadDao {

    private static Logger persistLogger = LogHelper.getPersistLogger();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    @Autowired
    private HadOddsRepository hadOddsRepository;

    @Autowired
    private HadOddsHistoryRepository hadOddsHistoryRepository;

    @Override
    public void saveOrUpdateHadOdds(List<HadOdds> queryHadList, List<HadOddsHistory> hadList, Long id) {

        for (HadOdds hadOdds : queryHadList) {
            hadOdds.setWin310Id(id);
        }
        for (HadOddsHistory history : hadList) {
            history.setWin310Id(id);
        }
        if (!CollectionUtils.isEmpty(hadList)) {
            HadOdds newHad = new HadOdds().from(hadList.get(0));
            if (newHad.getOddsA().equals("")) {
                if (hadList.size() > 1) {
                    newHad = new HadOdds().from(hadList.get(1));
                } else {
                    LogHelper.infoLog(persistLogger, null, "invalid {0}", newHad);
                    return;
                }
            }
            if (CollectionUtils.isEmpty(queryHadList)) {// no results
                hadOddsRepository.save(newHad);
                LogHelper.infoLog(persistLogger, null, "insert {0}", newHad);
                hadOddsHistoryRepository.save(hadList);
                LogHelper.infoLog(persistLogger, null, "insert {0}", hadList);
            } else {
                HadOdds hadOdds = queryHadList.get(0);
                if (!newHad.equals(hadOdds)) {
                    newHad.setId(hadOdds.getId());
                    dealSpecialDurationTime(newHad, hadOdds);
                    hadOddsRepository.save(newHad);
                    LogHelper.infoLog(persistLogger, null, "update {0}", newHad);
                    HadOddsHistory newHistory = new HadOddsHistory(newHad);
                    hadOddsHistoryRepository.save(newHistory);
                    LogHelper.infoLog(persistLogger, null, "insert {0}", newHistory);
                }
            }
        } else {
            HadOdds hadOdds;
            if (CollectionUtils.isEmpty(queryHadList)) {
                hadOdds = new HadOdds();
            } else {
                hadOdds = queryHadList.get(0);
                hadOdds.setId(hadOdds.getId());
                hadOdds.setDurationTime("");
                hadOdds.setOddsA("");
                hadOdds.setOddsD("");
                hadOdds.setScore("");
                hadOdds.setOddsH("");
                hadOdds.setState("");
                hadOdds.setOddsUpdateTime("");
            }
            hadOddsRepository.save(hadOdds);
            LogHelper.errorLog(errorLogger, null, "had odds is empty, win310 id is {0}", id);
        }
    }

    private void dealSpecialDurationTime(HadOdds newHad, HadOdds hadOdds) {

    /*
    金宝博公司在中场时，时间会改为00，所以对00特殊处理，变为上一条记录的时间
    不能变为46，因为开赛之前也可能有几条记录时间为00
    */
        String durationTime = newHad.getDurationTime();
        if ("00".equals(durationTime)) {
            newHad.setDurationTime(hadOdds.getDurationTime());
        } else if (StringUtils.isNotBlank(durationTime)) {
            //利记公司中场会变成“中场”
            if (durationTime.contains("中")) {
                newHad.setDurationTime("46");
            }
        }
    }
}
