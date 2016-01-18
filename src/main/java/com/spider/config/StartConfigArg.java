package com.spider.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wsy on 2016/1/15.
 *
 * @author wsy
 */
public class StartConfigArg {

    public static final List<String> ARG_ARRAY = Arrays.asList("500", "jbb", "lj", "jbb3", "lj3", "statistic", "win310", "pinnacle", "sportteryAll", "sporttery");

    private static List<String> args = new ArrayList<>();

    public static List<String> getArgs() {

        return args;
    }

    public static void setArgs(List<String> args) {

        StartConfigArg.args = args;
    }

    public static boolean check(String[] argArr) throws IllegalArgumentException {

        for (String arg : argArr) {
            if (!ARG_ARRAY.contains(arg)) {
                System.out.println("[" + arg + "] is invalid argument");
                System.out.println("valid arguments is " + ARG_ARRAY);
                return false;
            }
        }
        return true;
    }
}
