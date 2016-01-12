package com.spider.global;

/**
 * 爬虫服务枚举
 * <p/>
 * 每个枚举代表一个爬虫服务，目前有五个需要统计的服务
 *
 * @author wsy
 */
public enum ServiceName {

    /**
     * 利记的爬虫服务
     */
    LiJiRobot("LJ-Robot"),
    /**
     * 金宝博的爬虫服务
     */
    JinBaoBoRobot("JBB-Robot"),
    /**
     * 彩客的爬虫服务
     */
    Win310Robot("Win310-Robot"),
    /**
     * 官网的爬虫服务
     */
    SportteryRobot("Sporttery-Robot"),
    /**
     * 官网所有玩法的爬虫服务
     */
    SportteryAllRobot("Sporttery-All-Robot"),
    /**
     * 500万网站
     */
    W500("500-Robot");

    private final String name;

    ServiceName(String name) {

        this.name = name;
    }

    public String getName() {

        return this.name;
    }
}
