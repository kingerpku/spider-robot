package com.spider;

import com.spider.config.AppConfig;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 主程序入口，初始化spring容器
 * Created by wsy on 2015/10/14.
 *
 * @author wsy
 */
public class App {

    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        logger.info("app start success");
        logger.info(applicationContext.toString());
    }
}
