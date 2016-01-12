package com.spider.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 *
 * @author wsy
 */
public class LotteryUtils {

    public static String calcHadOrHhadMargin(String h, String d, String a) {

        if (StringUtils.isBlank(h) || StringUtils.isBlank(d) || StringUtils.isBlank(a)) {
            return "";
        }
        if (!isGoodFormatOdds(h) || !isGoodFormatOdds(d) || !isGoodFormatOdds(a)) {
            return "";
        }
        BigDecimal hNum = new BigDecimal(h);
        BigDecimal dNum = new BigDecimal(d);
        BigDecimal aNum = new BigDecimal(a);
        hNum = BigDecimal.ONE.divide(hNum, 5, RoundingMode.HALF_EVEN);
        dNum = BigDecimal.ONE.divide(dNum, 5, RoundingMode.HALF_EVEN);
        aNum = BigDecimal.ONE.divide(aNum, 5, RoundingMode.HALF_EVEN);
        BigDecimal result = new BigDecimal(0);
        result = result.add(hNum).add(dNum).add(aNum).multiply(new BigDecimal(100));
        return result.setScale(2, RoundingMode.HALF_EVEN).toString() + '%';
    }

    public static String calcHiloOrHdcMargin(String a, String b) {

        if (StringUtils.isBlank(a) || StringUtils.isBlank(b)) {
            return "";
        }
        if (!isGoodFormatOdds(a) || !isGoodFormatOdds(b)) {
            return "";
        }
        BigDecimal aNum = new BigDecimal(a);
        BigDecimal bNum = new BigDecimal(b);
        aNum = BigDecimal.ONE.divide(aNum, 5, RoundingMode.HALF_EVEN);
        bNum = BigDecimal.ONE.divide(bNum, 5, RoundingMode.HALF_EVEN);
        BigDecimal result = new BigDecimal(0);
        result = result.add(aNum).add(bNum).multiply(new BigDecimal(100));
        return result.setScale(2, RoundingMode.HALF_EVEN).toString() + '%';
    }

    public static boolean isGoodFormatOdds(String odds) {

        if (StringUtils.isBlank(odds)) {
            return false;
        }
        if (odds.contains("-")) {
            return false;
        }
        return true;
    }

    public static Date convertWin310MatchDateTime(String win310MatchDateTime) {

        Preconditions.checkArgument(!StringUtils.isBlank(win310MatchDateTime));
        Preconditions.checkState(Pattern.matches("\\d{2}-\\d{2}\\s\\d{2}:\\d{2}", win310MatchDateTime));
        String year = Calendar.getInstance().get(Calendar.YEAR) + "-";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(year + win310MatchDateTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String convertWin310MatchDate(String win310MatchDate) {

        Preconditions.checkArgument(!StringUtils.isBlank(win310MatchDate));
        String year = Calendar.getInstance().get(Calendar.YEAR) + "-";
        return year + win310MatchDate.replaceAll("\\s\\d{2}:\\d{2}", "");
    }

    private LotteryUtils() {

    }

    /**
     * @param oddsTwo
     * @return if ok return result added one, else return "-"
     */
    public static String addOneToOdds(String oddsTwo) {

        if (isGoodFormatOdds(oddsTwo)) {
            return new BigDecimal(oddsTwo).add(BigDecimal.ONE).setScale(2).toString();
        } else {
            return "-";
        }
    }
}
