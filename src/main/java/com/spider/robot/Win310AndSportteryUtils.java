package com.spider.robot;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.google.common.collect.Lists;
import com.spider.domain.GamingCompany;
import com.spider.entity.CompanyOddsEntity;
import com.spider.entity.HadOddsHistory;
import com.spider.entity.HdcOddsHistory;
import com.spider.entity.HiloOddsHistory;
import com.spider.fetcher.Fetcher;
import com.spider.fetcher.FetcherContent;
import com.spider.fetcher.HttpConfig;
import com.spider.selector.Selector;
import com.spider.selector.XpathSelector;
import com.spider.utils.Calendars;
import com.spider.utils.LotteryUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import static com.spider.global.Constants.JINBAOBO_NAME;
import static com.spider.global.Constants.LIJI_NAME;

/**
 * Created by wsy on 2015/11/4.
 */
public class Win310AndSportteryUtils {

    private static Logger logger = Logger.getLogger("info_logger");

    /**
     * 金宝博三合一赔率页面，字符串中的参数{0}表示比赛的europeId
     */
    private static final String jinbaoboPageUrlTemplate = "http://data.nowgoal.com/3in1odds/" + GamingCompany.JinBaoBo.getId() + "_{0}.html";

    //    private final String lijiPageUrlTemplate = "http://data.310win.com/changedetail/3in1Odds.aspx?id={0}&companyid=" + GamingCompany.LiJi.getId() + "&l=0";

    /**
     * 利记三合一赔率页面，字符串中的参数{0}表示比赛的europeId
     */
    private static final String lijiPageUrlTemplate = "http://data.nowgoal.com/3in1odds/" + GamingCompany.LiJi.getId() + "_{0}.html";

    public static String getCompetitionNum(String originNum, String week) {

        if ("周一".equals(week)) {
            return 1 + originNum;
        } else if ("周二".equals(week)) {
            return 2 + originNum;
        } else if ("周三".equals(week)) {
            return 3 + originNum;
        } else if ("周四".equals(week)) {
            return 4 + originNum;
        } else if ("周五".equals(week)) {
            return 5 + originNum;
        } else if ("周六".equals(week)) {
            return 6 + originNum;
        } else if ("周日".equals(week)) {
            return 7 + originNum;
        }
        return "";
    }

    public static String transSportteryMatchCode(String competitionNum) {

        String flag = competitionNum.substring(0, 2);
        competitionNum = competitionNum.replaceAll("\\D", "");

        if (flag.equals("周一")) {
            competitionNum = "1" + competitionNum;
        } else if (flag.equals("周二")) {
            competitionNum = "2" + competitionNum;
        } else if (flag.equals("周三")) {
            competitionNum = "3" + competitionNum;
        } else if (flag.equals("周四")) {
            competitionNum = "4" + competitionNum;
        } else if (flag.equals("周五")) {
            competitionNum = "5" + competitionNum;
        } else if (flag.equals("周六")) {
            competitionNum = "6" + competitionNum;
        } else if (flag.equals("周日")) {
            competitionNum = "7" + competitionNum;
        }
        return competitionNum;
    }

    /**
     * 获得今天的前一天的星期表示，例如周三，返回周二
     *
     * @param weekStr
     * @return
     */
    public static String getBeforeWeek(String weekStr) {

        if ("周一".equals(weekStr)) {
            return "周日";
        } else if ("周二".equals(weekStr)) {
            return "周一";
        } else if ("周三".equals(weekStr)) {
            return "周二";
        } else if ("周四".equals(weekStr)) {
            return "周三";
        } else if ("周五".equals(weekStr)) {
            return "周四";
        } else if ("周六".equals(weekStr)) {
            return "周五";
        } else if ("周日".equals(weekStr)) {
            return "周六";
        }
        return "";
    }

    public static String getWin310EuropeId(String europeLink) {

        String win310EuropeId = europeLink;
        win310EuropeId = win310EuropeId.substring(win310EuropeId.lastIndexOf("/") + 1);
        win310EuropeId = win310EuropeId.replace(".html", "");
        return win310EuropeId;
    }

    public static String getOddsHtml(String asiaLink, Fetcher fetcher, HttpConfig httpConfig, HttpHost proxy) {

        FetcherContent fetcherContent = null;
        String html = null;
        for (int i = 0; i < 3; i++) {
            fetcherContent = fetcher.wgetHtml(asiaLink, httpConfig, proxy);
            if (fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK) {
                break;
            } else if (fetcherContent != null
                    && (fetcherContent.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY || fetcherContent.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)) {
                fetcherContent.setUrl(fetcherContent.getRedirectUrl());
            }
        }
        if (fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK) {
            if ((html = fetcherContent.getHtml()) == null) {
                logger.error("get url [" + asiaLink + "] failed");
                throw new NullPointerException("get html time out");
            }
        }
        return html;
    }

    public static List<HadOddsHistory> parseHadOdds(HtmlTable table, String compangName, Integer europeId) {

        List<HadOddsHistory> list = Lists.newArrayList();
        List<HtmlTableRow> trs = table.getRows();

        for (int i = 2; i < trs.size(); i++) {// 第一行是表头，越过
            HadOddsHistory oddsHistory = new HadOddsHistory();
            HtmlTableRow tr = trs.get(i);
            String score = ((DomText) tr.getByXPath("td[2]/text()").get(0)).getWholeText();
            if (score.contains("比分")) {
                continue;
            } else {
                oddsHistory.setScore(score);
            }
            List<?> redCards = tr.getByXPath("td[2]/font");
            if (redCards.size() == 1) {
                //rtodo 主队的红牌还是客队的红牌
            } else if (redCards.size() == 2) {
                oddsHistory.setHomeRedCard(Integer.valueOf(((HtmlFont) redCards.get(0)).asText()));
                oddsHistory.setAwayRedCard(Integer.valueOf(((HtmlFont) redCards.get(1)).asText()));
            } else {
                oddsHistory.setHomeRedCard(0);
                oddsHistory.setAwayRedCard(0);
            }
            String durationTime = "";
            List<?> durationTimeList = tr.getByXPath("td[1]/text()");
            if (durationTimeList.size() != 0) {
                durationTime = ((DomText) durationTimeList.get(0)).getWholeText();
            }
            String odds1 = getOdds(tr, "td[3]/text()");
            String odds2 = getOdds(tr, "td[4]/text()");
            String odds3 = getOdds(tr, "td[5]/text()");
            String updateTime = ((HtmlScript) tr.getByXPath("td[6]/script").get(0)).asText();
            String state = ((DomText) tr.getByXPath("td[7]/text()").get(0)).getWholeText();
            oddsHistory.setOddsH(odds1);
            oddsHistory.setOddsD(odds2);
            oddsHistory.setOddsA(odds3);
            oddsHistory.setDurationTime(durationTime);
            oddsHistory.setOddsUpdateTime(dealNowgoalsUpdateTime(updateTime));
            oddsHistory.setState(state);
            oddsHistory.setGamingCompany(compangName);
            oddsHistory.setEuropeId(europeId);
            list.add(oddsHistory);
        }
        return list;
    }

    public static Map<Integer, List<CompanyOddsEntity>> parseOdds(String companyName, Integer europeId) throws IOException {

        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        Map<Integer, List<CompanyOddsEntity>> result = new HashMap<>();
        try {
            String url = null;
            if (JINBAOBO_NAME.equals(companyName)) {
                url = MessageFormat.format(jinbaoboPageUrlTemplate, europeId);
            } else if (LIJI_NAME.equals(companyName)) {
                url = MessageFormat.format(lijiPageUrlTemplate, europeId);
            }
            HtmlPage htmlPage = webClient.getPage(url);
            List<HtmlTable> tables = (List<HtmlTable>) htmlPage.getByXPath("//table[@class='gts']");
            if (tables.size() == 0) {
                return result;
            }

            for (int i = 0; i < tables.size(); i++) {
                List<CompanyOddsEntity> list = Lists.newArrayList();
                List<HtmlTableRow> trs = tables.get(i).getRows();
                for (int j = 2; j < trs.size(); j++) {// 第一行是表头，越过
                    CompanyOddsEntity odds = new CompanyOddsEntity();
                    HtmlTableRow tr = trs.get(j);
                    String score = ((DomText) tr.getByXPath("td[2]/text()").get(0)).getWholeText();
                    if (score.contains("比分")) {
                        continue;
                    } else {
                        odds.setScore(score);
                    }
                    List<?> redCards = tr.getByXPath("td[2]/font");
                    if (redCards.size() == 1) {
                        //rtodo 主队的红牌还是客队的红牌
                    } else if (redCards.size() == 2) {
                        odds.setHomeRedCard(Integer.valueOf(((HtmlFont) redCards.get(0)).asText()));
                        odds.setAwayRedCard(Integer.valueOf(((HtmlFont) redCards.get(1)).asText()));
                    } else {
                        odds.setHomeRedCard(0);
                        odds.setAwayRedCard(0);
                    }
                    String durationTime = "";
                    List<?> durationTimeList = tr.getByXPath("td[1]/text()");
                    if (durationTimeList.size() != 0) {
                        durationTime = ((DomText) durationTimeList.get(0)).getWholeText();
                    }
                    String odds1 = getOdds(tr, "td[3]/text()");
                    String odds2 = getOdds(tr, "td[4]/text()");
                    String odds3 = getOdds(tr, "td[5]/text()");
                    String updateTime = ((HtmlScript) tr.getByXPath("td[6]/script").get(0)).asText();
                    String state = ((DomText) tr.getByXPath("td[7]/text()").get(0)).getWholeText();
                    odds.setOddsOne(odds1);
                    odds.setOddsTwo(odds2);
                    odds.setOddsThree(odds3);
                    odds.setDurationTime(durationTime);
                    odds.setOddsUpdateTime(dealNowgoalsUpdateTime(updateTime));
                    odds.setState(state);
                    odds.setGamingCompany(companyName);
                    odds.setEuropeId(europeId);
                    odds.setOddsType(i);
                    list.add(odds);
                }
                result.put(i, list);
            }
        } finally {
            webClient.closeAllWindows();
        }
        return result;
    }

    private static String getOdds(HtmlTableRow tr, String xpath) {

        String odds = "";
        List<?> oddsAList = tr.getByXPath(xpath);
        if (oddsAList.size() != 0) {
            odds = ((DomText) oddsAList.get(0)).getWholeText();
        }
        return odds;
    }

    public static List<HdcOddsHistory> parseHdcOdds(String hdcHtml, String companyName, Integer europeId) {

        List<HdcOddsHistory> list = new ArrayList<>();
        if (StringUtils.isBlank(hdcHtml)) {
            return list;
        }
        Selector tablesSelector = new XpathSelector("//table[@class=gts]");
        Selector timeSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[1]/text()");// 比赛进行时间
        Selector scoreSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[2]/text()");
        Selector redCardSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[2]");
        Selector oddsOneSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[3]/text()");
        Selector handicapLineSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[4]/text()");
        Selector oddsTwoSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[5]/text()");
        Selector updateSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[6]/script");
        Selector stateSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[7]/text()");
        List<String> tables = tablesSelector.selectList(hdcHtml);
        Selector trsSelector = new XpathSelector("//table[@class=gts][1]/tbody/tr");
        int tableIndex = 1;
        List<String> trs = trsSelector.selectList("<html><body>" + tables.get(tableIndex) + "</body></html>");
        for (int i = 2; i < trs.size(); i++) {// 第一行是表头，越过
            String html = "<html><body><table id='xxx'>" + trs.get(i) + "</table></body></html>";
            HdcOddsHistory hdcOddsHistory = new HdcOddsHistory();
            hdcOddsHistory.setEuropeId(europeId);
            String score = scoreSelector.select(html);
            if (score.contains("比分")) {
                continue;
            } else {
                hdcOddsHistory.setScore(score);
            }
            String redCard = redCardSelector.select(html);
            if (redCard.contains("font")) {//有红牌
                if (redCard.contains("td><font")) {//主队红牌
                    String cardNumber = new XpathSelector("font/text()").select(redCard);
                    hdcOddsHistory.setHomeRedCard(Integer.valueOf(StringUtils.isBlank(cardNumber) ? "0" : cardNumber));
                } else if (redCard.contains("/font></td")) {//客队红牌
                    String cardNumber = new XpathSelector("font/text()").select(redCard);
                    hdcOddsHistory.setAwayRedCard(Integer.valueOf(StringUtils.isBlank(cardNumber) ? "0" : cardNumber));
                }
            }
            hdcOddsHistory.setDurationTime(timeSelector.select(html));
            hdcOddsHistory.setGamingCompany(companyName);
            hdcOddsHistory.setHandicapLine(handicapLineSelector.select(html));
            String oddsOne = oddsOneSelector.select(html);
            hdcOddsHistory.setOddsOne(LotteryUtils.addOneToOdds(oddsOne));
            String oddsTwo = oddsTwoSelector.select(html);
            hdcOddsHistory.setOddsTwo(LotteryUtils.addOneToOdds(oddsTwo));
            hdcOddsHistory.setOddsUpdateTime(dealNowgoalsUpdateTime(updateSelector.select(html)));
            hdcOddsHistory.setState(stateSelector.select(html));
            list.add(hdcOddsHistory);
        }
        return list;
    }

    public static List<HiloOddsHistory> parseHiloOdds(String jbbHtml, String compangName, Integer europeId) {

        List<HiloOddsHistory> set = Lists.newArrayList();
        if (StringUtils.isBlank(jbbHtml)) {
            return set;
        }
        Selector tablesSelector = new XpathSelector("//table[@class=gts]");
        Selector timeSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[1]/text()");// 比赛进行时间
        Selector scoreSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[2]/text()");
        Selector redCardSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[2]");
        Selector oddsHighSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[3]/text()");
        Selector oddsEqualSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[4]/text()");
        Selector oddsLowSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[5]/text()");
        Selector updateSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[6]/script");
        Selector stateSelector = new XpathSelector("//table[@id=xxx][1]/tbody/tr/td[7]/text()");
        List<String> tables = tablesSelector.selectList(jbbHtml);
        Selector trsSelector = new XpathSelector("//table[@class=gts][1]/tbody/tr");
        int tableIndex = 2;
        List<String> trs = trsSelector.selectList("<html><body>" + tables.get(tableIndex) + "</body></html>");
        for (int i = 2; i < trs.size(); i++) {// 第一行是表头，越过
            String html = "<html><body><table id='xxx'>" + trs.get(i) + "</table></body></html>";
            HiloOddsHistory hlOddsHistory = new HiloOddsHistory();
            hlOddsHistory.setEuropeId(europeId);
            String score = scoreSelector.select(html);
            if (score.contains("比分")) {
                continue;
            } else {
                hlOddsHistory.setScore(score);
            }
            String redCard = redCardSelector.select(html);
            if (redCard.contains("font")) {//有红牌
                if (redCard.contains("td><font")) {//主队红牌
                    String cardNumber = new XpathSelector("font/text()").select(redCard);
                    hlOddsHistory.setHomeRedCard(Integer.valueOf(StringUtils.isBlank(cardNumber) ? "0" : cardNumber));
                } else if (redCard.contains("/font></td")) {//客队红牌
                    String cardNumber = new XpathSelector("font/text()").select(redCard);
                    hlOddsHistory.setAwayRedCard(Integer.valueOf(StringUtils.isBlank(cardNumber) ? "0" : cardNumber));
                }
            }
            hlOddsHistory.setDurationTime(timeSelector.select(html));
            hlOddsHistory.setGamingCompany(compangName);
            hlOddsHistory.setHandicapLine(oddsEqualSelector.select(html));
            String oddsHigh = oddsHighSelector.select(html);
            hlOddsHistory.setOddsHigh(LotteryUtils.addOneToOdds(oddsHigh));
            String oddsLow = oddsLowSelector.select(html);
            hlOddsHistory.setOddsLow(LotteryUtils.addOneToOdds(oddsLow));
            hlOddsHistory.setOddsUpdateTime(dealNowgoalsUpdateTime(updateSelector.select(html)));
            hlOddsHistory.setState(stateSelector.select(html));
            set.add(hlOddsHistory);
        }
        return set;

    }

    private static String dealNowgoalsUpdateTime(String updateTime) {

        updateTime = updateTime.replaceAll("<script>showDate\\(", "").replaceAll("\\)</script>", "");
        return updateTime;
    }

    public static String getTimeMinute(String matchCode) throws IOException {

        String min = null;
        int count = 3;
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        try {
            while (count-- >= 0 || min == null) {
                HtmlPage htmlPage = webClient.getPage("http://live.500.com/");
                webClient.waitForBackgroundJavaScript(3000);
                try {
                    min = ((DomText) htmlPage.getByXPath("//table[@id='table_match']/tbody/tr[@order='" + matchCode + "']/td[5]/text()").get(0)).getWholeText();
                } catch (Exception e) {
                    min = ((DomText) htmlPage.getByXPath("//table[@id='table_match']/tbody/tr[@order='" + matchCode + "']/td[5]/span/text()").get(0)).getWholeText();
                }
            }
        } finally {
            webClient.closeAllWindows();
        }
        return min.replaceAll("'", "");
    }
}
