package com.spider.dao.impl;

import com.spider.dao.GamingCompanyHdcDao;
import com.spider.entity.HdcOdds;
import com.spider.entity.HdcOddsHistory;
import com.spider.repository.HdcOddsHistoryRepository;
import com.spider.repository.HdcOddsRepository;
import com.spider.utils.LogHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsy on 2015/12/9.
 *
 * @author wsy
 */
@Service
public class GamingCompanyHdcDaoImpl implements GamingCompanyHdcDao {

    private static Logger persistLogger = LogHelper.getPersistLogger();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    @Autowired
    private HdcOddsRepository hdcOddsRepository;

    @Autowired
    private HdcOddsHistoryRepository hdcOddsHistoryRepository;

    @Override
    @Transactional
    public HdcOdds saveOrUpdateHdcOdds(List<HdcOdds> queryHdcList, List<HdcOddsHistory> hdcList, Long id, String company) {

        for (HdcOdds hdcOdds : queryHdcList) {
            hdcOdds.setWin310Id(id);
        }
        for (HdcOddsHistory history : hdcList) {
            history.setWin310Id(id);
        }
        HdcOdds result = null;
        if (!CollectionUtils.isEmpty(hdcList)) {
            HdcOdds newHdc = new HdcOdds().from(hdcList.get(0));
            if (newHdc.getOddsOne().equals("")) {
                if (hdcList.size() > 1) {
                    newHdc = new HdcOdds().from(hdcList.get(1));
                } else {
                    LogHelper.infoLog(persistLogger, null, "invalid {0}", newHdc);
                    return result;
                }
            }
            if (CollectionUtils.isEmpty(queryHdcList)) {// no results
                hdcOddsRepository.save(newHdc);
                result = newHdc;
                LogHelper.infoLog(persistLogger, null, "insert {0}", newHdc);
                hdcOddsHistoryRepository.save(hdcList);
                LogHelper.infoLog(persistLogger, null, "insert {0}", newHdc);
            } else {
                HdcOdds hdcOdds = queryHdcList.get(0);
                if (!newHdc.equals(hdcOdds)) {
                    newHdc.setId(hdcOdds.getId());
                    dealSpecialDurationTime(newHdc, hdcOdds);
                    hdcOddsRepository.save(newHdc);
                    result = newHdc;
                    LogHelper.infoLog(persistLogger, null, "update {0}", newHdc);
                    HdcOddsHistory newHistory = new HdcOddsHistory(newHdc);
                    hdcOddsHistoryRepository.save(newHistory);
                    LogHelper.infoLog(persistLogger, null, "insert {0}", newHistory);
                }
            }
        } else {
            HdcOdds hdcOdds;
            if (CollectionUtils.isEmpty(queryHdcList)) {
                hdcOdds = new HdcOdds();
            } else {
                hdcOdds = queryHdcList.get(0);
                hdcOdds.setId(hdcOdds.getId());
                hdcOdds.setDurationTime("");
                hdcOdds.setOddsOne("");
                hdcOdds.setOddsTwo("");
                hdcOdds.setScore("");
                hdcOdds.setHandicapLine("");
                hdcOdds.setState("");
                hdcOdds.setOddsUpdateTime("");
            }
            hdcOddsRepository.save(hdcOdds);
            LogHelper.errorLog(errorLogger, null, "hdc odds is empty, win310 id is {0}", id);
        }
        return result;
    }

    private void dealSpecialDurationTime(HdcOdds newHdc, HdcOdds hdcOdds) {

        /**
         金宝博公司在中场时，时间会改为00，所以对00特殊处理，变为上一条记录的时间
         不能变为46，因为开赛之前也可能有几条记录时间为00
         */
        String durationTime = newHdc.getDurationTime();
        if ("00".equals(durationTime)) {
            newHdc.setDurationTime(hdcOdds.getDurationTime());
        } else if (StringUtils.isNotBlank(durationTime)) {//利记公司中场会变成“中场”
            if (durationTime.contains("中")) {
                newHdc.setDurationTime("46");
            }
        }
    }
}
