package com.spider.utils;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.spider.utils.LotteryUtils;
import org.junit.Test;

/**
 * 
 * @author wsy
 *
 */
public class LotteryUtilsTest {

    @Test
    public void testCalcHadOrHhadMargin() {

        String h = "1.68";
        String d = "3.1";
        String a = "4.9";
        String result = LotteryUtils.calcHadOrHhadMargin(h, d, a);
        assertEquals(result, "112.19%");
    }

    @Test(expected = NumberFormatException.class)
    public void testCalcHiloOrHdcMarginNumberFormatException() {

        String a = "fjk.d";
        String b = "0.84";
        LotteryUtils.calcHiloOrHdcMargin(a, b);
    }

    @Test
    public void testCalcHiloOrHdcMargin() {

        String a = "2.06";
        String b = "1.84";
        String result = LotteryUtils.calcHiloOrHdcMargin(a, b);
        assertEquals(result, "102.89%");
    }

    @Test(expected = NumberFormatException.class)
    public void testCalcHadOrHhadMarginNumberFormatException() {

        String h = "fds";
        String d = "3.1";
        String a = "4.9";
        LotteryUtils.calcHadOrHhadMargin(h, d, a);
    }

    @Test
    public void testConvertWin310MatchTime() throws ParseException {

        String str = "08-07 10:00";
        assertTrue(LotteryUtils.convertWin310MatchDateTime(str).equals(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-08-07 10:00")));
    }
}
