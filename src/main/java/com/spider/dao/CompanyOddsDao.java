package com.spider.dao;


import com.spider.entity.CompanyOddsEntity;

import java.util.List;

/**
 * Created by wsy on 2016/1/8.
 */
public interface CompanyOddsDao {

    void saveOrUpdate(List<CompanyOddsEntity> list, Integer oddsType);
}
