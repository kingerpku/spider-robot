/**
 * @Description
 * @auth guowang
 * @time 2014-12-9
 */
package com.spider.utils;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Priority;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author guowang
 */
@Component
public class MyDailyRollingFileAppender extends DailyRollingFileAppender {

//    public MyDailyRollingFileAppender(Layout layout, String datePattern) throws IOException {
//
//        super(layout, "log" + File.separator + Thread.currentThread().getName(), datePattern);
//    }

    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {

        // 只判断是否相等，而不判断优先级
        return this.getThreshold().equals(priority);
    }
}
