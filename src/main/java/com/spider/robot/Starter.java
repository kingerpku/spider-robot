package com.spider.robot;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 启动所有抓取任务
 *
 * @author wsy
 */
@Component
public class Starter {

    /**
     * 默认定时任务的调度时间，单位是秒
     */
    public static final int DEFAULT_SCHEDULE_PERIOD = 30;

    public static final int INITIAL_DELAY = 5;

    private Logger appLogger = Logger.getLogger("app_logger");

    private ScheduledExecutorService scheduleExecutor = Executors.newScheduledThreadPool(8);

    @Autowired
    @Qualifier("jbbParser")
    private ThreeInOneParser jbbParser;

    @Autowired
    @Qualifier("ljParser")
    private ThreeInOneParser ljParser;

    @Autowired
    private StatisticRobot statisticRobot;

    @Autowired
    private PinnaclesRobot pinnaclesRobot;

    @Autowired
    private Win310Robot win310Robot;

    @Autowired
    private W500Robot w500Robot;

    @Autowired
    @Qualifier("jbbRobot")
    private CompanyOddsRobot jbbRobot;

    @Autowired
    @Qualifier("ljRobot")
    private CompanyOddsRobot ljRobot;

    @PostConstruct
    public void start() {

        scheduleExecutor.scheduleWithFixedDelay(win310Robot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("win310 robot start...");
        scheduleExecutor.scheduleWithFixedDelay(jbbParser, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("jbb robot start...");
        scheduleExecutor.scheduleWithFixedDelay(ljParser, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("lj robot start...");
        scheduleExecutor.scheduleWithFixedDelay(pinnaclesRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("pinnacle robot start...");
        scheduleExecutor.scheduleWithFixedDelay(w500Robot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("w500 robot start...");
        scheduleExecutor.scheduleWithFixedDelay(jbbRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("jbb robot start...");
        scheduleExecutor.scheduleWithFixedDelay(ljRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
        appLogger.info("lj robot start...");
        scheduleExecutor.scheduleWithFixedDelay(statisticRobot, INITIAL_DELAY, 5, TimeUnit.MINUTES);
        appLogger.info("statistic robot start...");
    }
}
