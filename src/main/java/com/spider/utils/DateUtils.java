package com.spider.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtils {

    private static final Logger logger = Logger.getLogger("error_logger");

    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getDate(String timeType) {

        String strTime = null;
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(timeType);
            strTime = simpledateformat.format(new Date());
        } catch (Exception ex) {
            logger.error("格式化日期错误 : " + ex.getMessage());
        }
        return strTime;
    }

    public static String getDate() {

        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    public static synchronized Date parseDateFormat(String strDate) {

        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseDateFormat(strDate, pattern);
    }

    public static Date parseDateFormat(String strDate, String pattern) {

        synchronized (sdf) {
            Date date = null;
            sdf.applyPattern(pattern);
            try {
                date = sdf.parse(strDate);
            } catch (Exception localException) {
            }
            return date;
        }
    }

    public static Timestamp fromFormatStr(String str, String parttern) {

        Timestamp timestamp = null;
        try {
            timestamp = new Timestamp(new SimpleDateFormat(parttern).parse(str).getTime());
        } catch (ParseException e) {
            return null;
        }
        return timestamp;
    }

    public static long diffAbsMills(Date startDateTime, Date startDateTime1) {


        return Math.abs(startDateTime.getTime() - startDateTime1.getTime());
    }

    public static String format(Date date, String s) {

        SimpleDateFormat format = new SimpleDateFormat(s);
        return format.format(date);
    }
}