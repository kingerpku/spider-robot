package com.spider.robot;

import com.spider.config.StartConfigArg;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
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

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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

    @Autowired
    private SportteryAllRobot sportteryAllRobot;

    @Autowired
    private SportteryRobot sportteryRobot;

    @PostConstruct
    public void start() {

        List<String> args = StartConfigArg.getArgs();
        if (args.size() == 0) {
            StartConfigArg.setArgs(StartConfigArg.ARG_ARRAY);
            args = StartConfigArg.getArgs();
        }
        if (args.contains("win310")) {
            scheduleExecutor.scheduleWithFixedDelay(win310Robot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
            System.out.println("win310 robot start...");
        }
        if (args.contains("jbb3")) {
            scheduleExecutor.scheduleWithFixedDelay(jbbParser, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
            System.out.println("jbb robot start...");
        }
        if (args.contains("lj3")) {
            scheduleExecutor.scheduleWithFixedDelay(ljParser, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
            System.out.println("lj robot start...");
        }
        if (args.contains("pinnacle")) {
            scheduleExecutor.scheduleWithFixedDelay(pinnaclesRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
            System.out.println("pinnacle robot start...");
        }
        if (args.contains("500")) {
            executorService.submit(w500Robot);
            System.out.println("w500 robot start...");
        }
        if (args.contains("jbb")) {
            scheduleExecutor.scheduleWithFixedDelay(jbbRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
            System.out.println("jbb robot start...");
        }
        if (args.contains("lj")) {
            scheduleExecutor.scheduleWithFixedDelay(ljRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.SECONDS);
            System.out.println("lj robot start...");
        }
        if (args.contains("statistic")) {
            scheduleExecutor.scheduleWithFixedDelay(statisticRobot, 0, 5, TimeUnit.MINUTES);
            System.out.println("statistic robot start...");
        }
        if (args.contains("sportteryAll")) {
            scheduleExecutor.scheduleWithFixedDelay(sportteryAllRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.MINUTES);
            System.out.println("sportteryAll robot start...");
        }
        if (args.contains("sporttery")) {
            scheduleExecutor.scheduleWithFixedDelay(sportteryRobot, INITIAL_DELAY, DEFAULT_SCHEDULE_PERIOD, TimeUnit.MINUTES);
            System.out.println("sporttery robot start...");
        }
    }
}
