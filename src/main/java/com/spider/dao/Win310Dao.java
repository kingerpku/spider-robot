package com.spider.dao;

import java.util.List;

import com.spider.entity.*;

public interface Win310Dao {

    /**
     * 负责处理彩客网赔率的持久化工作
     *
     * @param win310
     * @return 有更新返回true，否则返回false
     */
    boolean compareAndUpdateWin310(TCrawlerWin310 win310);

}