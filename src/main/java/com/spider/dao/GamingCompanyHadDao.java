package com.spider.dao;

import com.spider.entity.HadOdds;
import com.spider.entity.HadOddsHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsy on 2015/12/9.
 */
public interface GamingCompanyHadDao {

    /**
     * 更新had的赔率，有更新存入数据库，并将旧数据存入历史表，如果没有变化，则无操作
     *
     * @param queryHadList
     * @param hadList
     * @param id
     */

    @Transactional
    void saveOrUpdateHadOdds(List<HadOdds> queryHadList, List<HadOddsHistory> hadList, Long id);
}
