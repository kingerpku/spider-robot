package com.spider.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wsy on 2015/12/22.
 */
public class RobotUtils {

    public static Long getUniqueMatchId(Date startDateTime, String matchCode) {

        return Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(startDateTime) + matchCode);
    }
}
