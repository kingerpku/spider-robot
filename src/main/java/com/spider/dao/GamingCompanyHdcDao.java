package com.spider.dao;

import com.spider.entity.HdcOdds;
import com.spider.entity.HdcOddsHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsy on 2015/12/9.
 *
 * @author wsy
 */
public interface GamingCompanyHdcDao {

    /**
     * 更新hdc的赔率，有更新存入数据库，并将旧数据存入历史表，如果没有变化，则无操作
     *
     * @param queryHdcList
     * @param hdcList
     * @param id
     * @param company
     * @return 如果有更新，返回最新的赔率对象
     */
    @Transactional
    HdcOdds saveOrUpdateHdcOdds(List<HdcOdds> queryHdcList, List<HdcOddsHistory> hdcList, Long id, String company);
}
