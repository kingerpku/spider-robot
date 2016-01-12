/**
 * @Description
 * @auth guowang  
 * @time 2014-12-26
 */
package com.spider.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * @author guowang
 */
public class MyTypeConvert {

    private static String class_name = MyTypeConvert.class.getName();

    private static Logger error_logger = Logger.getLogger("error_logger");

    public static int obj2int(Object obj) {

        int mun = 0;
        String mun_str = "";
        if (obj != null) {
            mun_str = obj + "";
            mun_str = mun_str.trim();
            if (mun_str.startsWith("-")) {
                mun_str = mun_str.substring(1).replaceAll("\\D", "");
                mun_str = "-" + mun_str;
            } else {
                mun_str = mun_str.replaceAll("\\D", "");
            }

        }
        if (StringUtils.isNotEmpty(mun_str) && NumberUtils.isNumber(mun_str)) {
            mun = Integer.parseInt(mun_str);
        }
        return mun;
    }

    public static long obj2long(Object obj) {

        long mun = 0;
        String mun_str = "";
        if (obj != null) {
            mun_str = obj + "";
            mun_str = mun_str.trim();
            if (mun_str.startsWith("-")) {
                mun_str = mun_str.substring(1).replaceAll("\\D", "");
                mun_str = "-" + mun_str;
            } else {
                mun_str = mun_str.replaceAll("\\D", "");
            }

        }
        if (StringUtils.isNotEmpty(mun_str) && NumberUtils.isNumber(mun_str)) {
            mun = Long.parseLong(mun_str);
        }
        return mun;
    }

    public static String obj2str(Object obj) {

        String str = "";
        if (obj != null) {
            str = (String) obj;
        }
        return str.trim();
    }

    public static Date obj2date(Object obj, String str, String pattern) {

        Date date = null;
        try {
            if (obj != null && StringUtils.isNotBlank(obj + "")) {
                date = DateUtils.parseDate(obj2str(obj), pattern);
            } else {
                date = DateUtils.parseDate(str, pattern);
            }
        } catch (Exception e) {
            LogHelper.errorLog(error_logger, e, "class:{0},method:{1},obj:{2}", class_name, "obj2date", obj);
            try {
                date = DateUtils.parseDate(str, pattern);
            } catch (Exception e1) {
                LogHelper.errorLog(error_logger, e1, "class:{0},method:{1},str:{2},pattern:{3}", class_name, "obj2date", str, pattern);
            }
        }
        return date;
    }
}
