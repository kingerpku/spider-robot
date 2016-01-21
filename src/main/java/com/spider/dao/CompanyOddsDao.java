package com.spider.dao;


import com.spider.entity.CompanyOddsEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsy on 2016/1/8.
 */
public interface CompanyOddsDao {

    @Transactional
    Long saveOrUpdate(List<CompanyOddsEntity> list, Integer oddsType, Integer europeId);
}
