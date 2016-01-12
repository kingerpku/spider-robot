package com.spider.robot.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.spider.global.SingleState;
import com.spider.robot.Win310AndSportteryUtils;
import com.spider.sbc.SbcUpdateManager;
import com.spider.utils.RobotUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spider.entity.TCrawlerWin310;

import static com.spider.global.Constants.NBSP_HTML;

import com.spider.utils.LotteryUtils;
import com.spider.dao.Win310Dao;
import com.spider.domain.Win310OriginalData;
import com.spider.selector.Selector;
import com.spider.selector.XpathSelector;
import com.spider.utils.LogHelper;
import com.spider.utils.MyTypeConvert;

/**
 * win310解析器，从原始带解析的html数据中解析出win310对象和对应的博彩公司的赔率信息
 *
 * @author wsy
 */
@Service
public class Win310Parser {

    public static final String ERROR = "[ERROR]";

    private static Logger errorLogger = LogHelper.getErrorLogger();

    private Logger logger = Logger.getLogger("info_logger");

    @Autowired
    private Win310Dao win310Dao;

    @Autowired
    private SbcUpdateManager sbcUpdateManager;

    public TCrawlerWin310 process(Win310OriginalData win310OriginalData) {

        TCrawlerWin310 win310 = parse(win310OriginalData);
        boolean isUpdated = win310Dao.compareAndUpdateWin310(win310);
        if (isUpdated) {
            String matchCode = win310.getCompetitionNum();
            sbcUpdateManager.updateSbcScoreAndHalf(win310, matchCode);
            sbcUpdateManager.updateMatchCode(matchCode);
        }
        return win310;
    }

    private TCrawlerWin310 parse(Win310OriginalData win310OriginalData) {

        Selector weekSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/@name");
        Selector competitionNumSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[1]/text()");
        Selector startTimeSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[3]/text()");
        Selector stopSaleTimeSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[4]/text()");
        Selector durationTimeSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[4]/font/text()");
        Selector matchsSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[2]/a/text()");
        Selector homeTeamSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[5]/a/text()");
        String win310EuropeId = win310OriginalData.getWin310EuropeId();
        Selector homeTeamRedCardSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[5]/span[@id=HomeRed_" + win310EuropeId + "]");
        Selector visitionTeamSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[8]/a/text()");
        Selector visitionTeamRedCardSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[8]/span[@id=GuestRed_" + win310EuropeId + "]");
        Selector scoreSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[7]/text()");
        Selector halfScoreSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[9]/text()");
        Selector asiaLinkSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[16]/a[1]/@href");
        Selector europeLinkeSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[16]/a[2]/@href");
        /*
         * 如果选出两行，说明had和hhad都有，否则只有hhad
         */
        Selector contentLineSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr");
        Selector winCountOneSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[1]/td[1]/b/font/text()");
        Selector winCountTwoSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[2]/td[1]/b/font/text()");
        Selector winProbabilityOneSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[1]/td[2]/span/text()");
        Selector drawProbabilityOneSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[1]/td[3]/span/text()");
        Selector failProbabilityOneSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[1]/td[4]/span/text()");
        Selector winProbabilityTwoSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[2]/td[2]/span/text()");
        Selector drawProbabilityTwoSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[2]/td[3]/span/text()");
        Selector failProbabilityTwoSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[17]/table/tbody/tr[2]/td[4]/span/text()");

        String html = win310OriginalData.getMainContent();
        TCrawlerWin310 bean = new TCrawlerWin310();
        try {
            String startDateTime = MyTypeConvert.obj2str(startTimeSelector.select(html));
            bean.setBDate(LotteryUtils.convertWin310MatchDate(startDateTime));
            Date stopSaleTime = getStopSaleTime(stopSaleTimeSelector.select(html));
            bean.setStopSaleTime(stopSaleTime);
            String competitionNum = getCompetitionNum(MyTypeConvert.obj2str(competitionNumSelector.select(html)), weekSelector.select(html));
            if (stopSaleTime == null) {
                bean.setDurationTime(durationTimeSelector.select(html));
                try {
//                    bean.setTimeMinute(Win310AndSportteryUtils.getTimeMinute(competitionNum));
                } catch (Exception e) {
                    logger.info(ERROR + "500.com time minute failed, match code is " + competitionNum);
                }
            }
            bean.setStartDateTime(LotteryUtils.convertWin310MatchDateTime(startDateTime));
            bean.setCompetitionNum(competitionNum);
            bean.setSingleState(win310OriginalData.getSingleMatchSet().contains(competitionNum) ? SingleState.Yes.value() : SingleState.No.value());
            bean.setStartTime(startDateTime);
            bean.setMatchs(MyTypeConvert.obj2str(matchsSelector.select(html)));
            bean.setHomeTeam(MyTypeConvert.obj2str(homeTeamSelector.select(html)));
            bean.setHomeRedCard(getRedCard(MyTypeConvert.obj2str(homeTeamRedCardSelector.select(html))));
            bean.setVisitionTeam(MyTypeConvert.obj2str(visitionTeamSelector.select(html)));
            bean.setGuestRedCard(getRedCard(MyTypeConvert.obj2str(visitionTeamRedCardSelector.select(html))));
            bean.setScore(MyTypeConvert.obj2str(scoreSelector.select(html)));
            bean.setHalfScore(MyTypeConvert.obj2str(halfScoreSelector.select(html)));
            String asiaLink = MyTypeConvert.obj2str(asiaLinkSelector.select(html));
            if (!asiaLink.startsWith("http://")) {
                asiaLink = "http://www.310win.com" + asiaLink;
            }
            bean.setAsiaLink(asiaLink);
            String europeLink = MyTypeConvert.obj2str(europeLinkeSelector.select(html));
            if (!europeLink.startsWith("http://")) {
                europeLink = "http://www.310win.com" + europeLink;
            }
            bean.setEuropeLink(europeLink);
            bean.setWin310EuropeId(win310EuropeId);
            String tmp = MyTypeConvert.obj2str(winCountOneSelector.select(html));
            tmp = tmp.replaceAll(NBSP_HTML, "");
            if (StringUtils.isBlank(tmp)) {
                tmp = "0";
            }

            List<String> trs = contentLineSelector.selectList(html);
            if (trs.size() > 1) {
                bean.setWinCountOne(Integer.parseInt(tmp));
                bean.setWinProbabilityOne(MyTypeConvert.obj2str(winProbabilityOneSelector.select(html)));
                bean.setDrawProbabilityOne(MyTypeConvert.obj2str(drawProbabilityOneSelector.select(html)));
                bean.setFailProbabilityOne(MyTypeConvert.obj2str(failProbabilityOneSelector.select(html)));
            } else {
                /**
                 * 如果只有一行，1的selector就是实际上2的selector，因为1对应的玩法没有开售
                 */
                String winCountTwo = MyTypeConvert.obj2str(winCountOneSelector.select(html));
                int winCountTwoInt = Integer.parseInt(winCountTwo);
                bean.setWinCountTwo(StringUtils.isBlank(winCountTwo) ? null : winCountTwoInt);
                bean.setWinProbabilityTwo(MyTypeConvert.obj2str(winProbabilityOneSelector.select(html)));
                bean.setDrawProbabilityTwo(MyTypeConvert.obj2str(drawProbabilityOneSelector.select(html)));
                bean.setFailProbabilityTwo(MyTypeConvert.obj2str(failProbabilityOneSelector.select(html)));
                bean.setUniqueId(RobotUtils.getUniqueMatchId(bean.getStartDateTime(), competitionNum));
                return bean;
            }

            tmp = MyTypeConvert.obj2str(winCountTwoSelector.select(html));
            tmp = tmp.replaceAll("\\s", "");
            if (StringUtils.isEmpty(tmp)) {
                tmp = "0";
            }
            tmp = tmp.replace("+", "");
            bean.setWinCountTwo(Integer.parseInt(tmp));
            bean.setWinProbabilityTwo(MyTypeConvert.obj2str(winProbabilityTwoSelector.select(html)));
            bean.setDrawProbabilityTwo(MyTypeConvert.obj2str(drawProbabilityTwoSelector.select(html)));
            bean.setFailProbabilityTwo(MyTypeConvert.obj2str(failProbabilityTwoSelector.select(html)));

            bean.setUniqueId(RobotUtils.getUniqueMatchId(bean.getStartDateTime(), competitionNum));
        } catch (Exception e) {
            LogHelper.errorLog(errorLogger, e, "", "");
            bean = null;
        }
        return bean;
    }

    private Integer getRedCard(String s) {

        if (StringUtils.isBlank(s)) {
            return null;
        }
        if (s.contains("/images/redcard1.gif")) {
            return 1;
        } else if (s.contains("/images/redcard2.gif")) {
            return 2;
        } else if (s.contains("/images/redcard3.gif")) {
            return 3;
        } else {
            return 0;
        }
    }

    private Date getStopSaleTime(String stopSaleTime) {

        if (StringUtils.isBlank(stopSaleTime)) {
            return null;
        }
        if (stopSaleTime.matches("\\d{2}\\-\\d{2}\\s\\d{2}:\\d{2}")) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(Calendar.getInstance().get(Calendar.YEAR) + "-" + stopSaleTime);
            } catch (ParseException e) {
                LogHelper.errorLog(errorLogger, e, "parse stop sale time error: {0}", stopSaleTime);
                return null;
            }
        }
        return null;
    }

    private static String getCompetitionNum(String originNum, String week) {

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

}
