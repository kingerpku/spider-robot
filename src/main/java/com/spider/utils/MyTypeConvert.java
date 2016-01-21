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

    public static String obj2str(Object obj) {

        String str = "";
        if (obj != null) {
            str = (String) obj;
        }
        return str.trim();
    }
}
