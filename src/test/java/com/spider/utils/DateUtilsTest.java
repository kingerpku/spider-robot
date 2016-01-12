package com.spider.utils;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by wsy on 2016/1/5.
 */
public class DateUtilsTest {

    @Test
    public void testDiffAbsMills() {

        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(72));

        assertTrue(DateUtils.diffAbsMills(date1, date2) > TimeUnit.HOURS.toMillis(71));
    }
}