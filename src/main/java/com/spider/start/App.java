package com.spider.start;

import com.spider.config.AppConfig;
import com.spider.config.StartConfigArg;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * 主程序入口，初始化spring容器，加载com.spider.robot.Starter后会启动抓取任务
 * 启动任务逻辑在{@link com.spider.robot.Starter}中
 *
 * @author wsy
 */
public class App {

    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {


        if (StartConfigArg.check(args)) {
            StartConfigArg.setArgs(Arrays.asList(args));
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
            System.out.println("app start success");
            System.out.println(applicationContext.toString());
        } else {
            System.out.println("start failed");
        }
    }
}
