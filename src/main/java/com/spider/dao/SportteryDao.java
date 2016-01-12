package com.spider.dao;

import java.util.List;

import com.spider.entity.SportteryAllEntity;
import com.spider.entity.TCrawlerSporttery;

/**
 * 关于官网的数据访问对象，负责t_crawler_sporttery表和sporttery_all表的持久化任务
 * 两个表分别有对应的历史数据表，表结构相同，作为历史数据的存储
 * 名字为t_crawler_sporttery_history和sporttery_all_history
 *
 * @author wsy
 */
public interface SportteryDao {

    /**
     * 遍历参数列表
     * 若库中没有，则插入
     * 若库中已存在，判断是否有更改，若有更改则覆盖，并将旧数据插入历史表，否则无操作
     *
     * @param beans
     */
    void compareAndUpdateBeans(List<TCrawlerSporttery> beans);

    /**
     * 遍历参数列表
     * 若库中没有，则插入
     * 若库中已存在，判断是否有更改，若有更改则覆盖，并将旧数据插入历史表，否则无操作
     *
     * @param beans
     */
    void compareAndUpdateSportteryAllBeans(List<SportteryAllEntity> beans);
}