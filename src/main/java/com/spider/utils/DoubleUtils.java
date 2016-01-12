package com.spider.utils;

/**
 * Created by wsy on 2015/10/23.
 */
public class DoubleUtils {

    public static Double parse(String num, Double defaultNum) {

        Double result;
        try {
            result = Double.parseDouble(num);
        } catch (NumberFormatException e) {
            return defaultNum;
        } catch (NullPointerException e) {
            return defaultNum;
        }
        return result;
    }

    public static boolean equalsZero(double homeOdds) {

        return homeOdds > -0.0001 && homeOdds < 0.0001;
    }
}
