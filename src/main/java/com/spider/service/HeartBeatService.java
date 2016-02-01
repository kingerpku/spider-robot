package com.spider.service;

import java.util.Date;

/**
 * 爬虫的心跳服务
 * <p/>
 * 对每个爬虫一次抓取的开始和结束时间进行记录，存入service_state和service_state_history表中
 * <p/>
 *
 * @author wsy
 */
public interface HeartBeatService {

    /**
     * 每次抓取任务结束时向数据库中存入运行相关信息，记录成功或失败情况
     *
     * @param serviceName 爬虫服务名称，参照{@link com.spider.global.ServiceName}
     * @param start       开始抓取时间
     * @param end         结束抓取时间
     * @param isSuccess   是否抓取成功
     * @param errorMsg    错误信息，可以null
     */
    void heartBeat(String serviceName, Date start, Date end, boolean isSuccess, String errorMsg);
}
