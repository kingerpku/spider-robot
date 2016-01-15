package com.spider.robot;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.spider.dao.StatisticDao;
import com.spider.entity.*;
import com.spider.global.EventType;
import com.spider.repository.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsy on 2015/12/23.
 * 赛事统计数据
 *
 * @author wsy
 */
@Component
public class StatisticRobot implements Runnable {

    public static final int HOME_TEAM_TYPE = 1;

    public static final int AWAY_TEAM_TYPE = 2;

    public static final String ERROR = "[ERROR]";

    private Logger logger = Logger.getLogger("info_logger");

    @Autowired
    TCrawlerWin310Repository tCrawlerWin310Repository;

    @Autowired
    private NowgoalKeyEventRepository nowgoalKeyEventRepository;

    @Autowired
    private NowgoalMatchPlayersRepository nowgoalMatchPlayersRepository;

    @Autowired
    private NowgoalMatchRepository nowgoalMatchRepository;

    @Autowired
    private StatisticDao statisticDao;

    private static WebClient webClient = new WebClient(BrowserVersion.CHROME);

    private static WebClient webClientJs = new WebClient(BrowserVersion.CHROME);

    {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClientJs.getOptions().setJavaScriptEnabled(true);
        webClientJs.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }


    //------------------------------------------------------------------
    public void goKeyEvent(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalKeyEvent> nowgoalKeyEvent = nowgoalKeyEventRepository.findByMatchId((long) europeId);
        if (nowgoalKeyEvent.size() != 0) {
            logger.info(europeId + " has existed in database");
            return;
        }
        List<NowgoalKeyEvent> nowgoalKeyEvents = getKeyEventList(europeId, htmlPage);
        for (NowgoalKeyEvent nke : nowgoalKeyEvents) {
            nowgoalKeyEventRepository.save(nke);
        }
    }

    private static List<NowgoalKeyEvent> getKeyEventList(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalKeyEvent> nowgoalKeyEvents = new ArrayList<>();
        DomElement keyEventsTable = getKeyEventsTable(htmlPage);
        if (keyEventsTable == null) {
            return nowgoalKeyEvents;
        }
        DomNodeList<HtmlElement> trs = keyEventsTable.getElementsByTagName("tr");
        HtmlElement scoreTr = trs.get(1);//比分在第二行
        String homeScore = scoreTr.getByXPath("td[1]/span/text()").get(0).toString();
        String awayScore = scoreTr.getByXPath("td[3]/span/text()").get(0).toString();
        for (int i = 2; i < trs.getLength(); i++) {
            HtmlElement tr = trs.get(i);
            NowgoalKeyEvent nowgoalKeyEvent = new NowgoalKeyEvent();
            nowgoalKeyEvent.setMatchId((long) europeId);
            Integer teamType = getTeamType(tr);
            nowgoalKeyEvent.setTeamType(teamType);
            Integer eventType = getEventType(tr, teamType);
            nowgoalKeyEvent.setEventType(eventType);
            nowgoalKeyEvent.setEventDesc(EventType.getDescById(eventType));
            nowgoalKeyEvent.setRelativePlayer(getRelativePlayer(tr, teamType, eventType));
            nowgoalKeyEvent.setTimeMinute(getTimeMinute(tr));
            nowgoalKeyEvent.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            nowgoalKeyEvents.add(nowgoalKeyEvent);
        }
        return nowgoalKeyEvents;
    }

    private static DomElement getKeyEventsTable(HtmlPage htmlPage) throws IOException {

        DomNodeList<DomElement> domNodeList = htmlPage.getElementsByTagName("table");
        for (DomElement domElement : domNodeList) {
            if (domElement.getAttribute("class").equals("bhTable")) {
                DomNodeList<HtmlElement> ths = domElement.getElementsByTagName("th");
                for (HtmlElement th : ths) {
                    if (th.getTextContent().contains("Key Events")) {
                        return domElement;
                    }
                }
            }
        }
        return null;
    }

    private static String getRelativePlayer(HtmlElement tr, Integer teamType, Integer eventType) {

        int cellIndex = 5;//默认取客场的cell
        if (teamType == HOME_TEAM_TYPE) {
            cellIndex = 1;//主场cell
        }
        if (eventType == EventType.Sub.getId()) {
            List anchors = tr.getByXPath("td[" + cellIndex + "]/a");
            return ((HtmlAnchor) anchors.get(0)).getTextContent() + "|" + ((HtmlAnchor) anchors.get(1)).getTextContent();
        } else {
            try {
                return ((DomText) tr.getByXPath("td[" + cellIndex + "]/a/text()").get(0)).getTextContent();
            } catch (Exception e) {
                return ((HtmlTableDataCell) tr.getByXPath("td[" + cellIndex + "]").get(0)).getTextContent();
            }
        }

    }

    private static Integer getEventType(HtmlElement tr, Integer teamType) {

        if (teamType == HOME_TEAM_TYPE) {
            String src = ((HtmlImage) tr.getByXPath("td[2]/img").get(0)).getSrcAttribute();
            return EventType.getIdByImageUrl(src);
        } else {
            String src = ((HtmlImage) tr.getByXPath("td[4]/img").get(0)).getSrcAttribute();
            return EventType.getIdByImageUrl(src);
        }
    }

    private static Integer getTimeMinute(HtmlElement tr) {

        return Integer.valueOf(((HtmlTableDataCell) tr.getByXPath("td[3]").get(0)).getTextContent().replaceAll("'", ""));
    }

    private static Integer getTeamType(HtmlElement tr) {

        return ((HtmlTableDataCell) tr.getByXPath("td[1]").get(0)).getTextContent().contains("\u00A0") ? 2 : 1;
    }
    //------------------------------------------------------------------

    //------------------------------------------------------------------
    private static DomElement getTechStatisticsTable(HtmlPage htmlPage) throws IOException {

        DomNodeList<DomElement> domNodeList = htmlPage.getElementsByTagName("table");
        for (DomElement domElement : domNodeList) {
            if (domElement.getAttribute("class").equals("bhTable")) {
                DomNodeList<HtmlElement> ths = domElement.getElementsByTagName("th");
                for (HtmlElement th : ths) {
                    if (th.getTextContent().contains("Tech Statistics")) {
                        return domElement;
                    }
                }
            }
        }
        return null;
    }

    private void goMatchStatistics(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalMatchStatisticEntity> statisticEntities = getNowgoalMatchStatisticEntities(europeId, htmlPage);
        statisticDao.updateMatchStatistic(europeId, statisticEntities);
    }

    private static List<NowgoalMatchStatisticEntity> getNowgoalMatchStatisticEntities(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalMatchStatisticEntity> statisticEntities = new ArrayList<>();
        DomElement techStatisticTable = getTechStatisticsTable(htmlPage);
        if (techStatisticTable == null) {
            return statisticEntities;
        }
        DomNodeList<HtmlElement> trs = techStatisticTable.getElementsByTagName("tr");
        for (int i = 1; i < trs.size(); i++) {
            HtmlElement tr = trs.get(i);
            String desc = ((DomText) tr.getByXPath("td[3]/text()").get(0)).getTextContent();
            if (CollectionUtils.isNotEmpty(tr.getByXPath("td[2]/img"))) {//主队特有的
                NowgoalMatchStatisticEntity homeEntity = new NowgoalMatchStatisticEntity();
                setBasicInfo(europeId, desc, homeEntity);
                homeEntity.setTeam(HOME_TEAM_TYPE);
                statisticEntities.add(homeEntity);
            } else if (CollectionUtils.isNotEmpty(tr.getByXPath("td[4]/img"))) {//客队特有的
                NowgoalMatchStatisticEntity awayEntity = new NowgoalMatchStatisticEntity();
                setBasicInfo(europeId, desc, awayEntity);
                awayEntity.setTeam(AWAY_TEAM_TYPE);
                statisticEntities.add(awayEntity);
            } else { //每个队都有的
                NowgoalMatchStatisticEntity homeEntity = new NowgoalMatchStatisticEntity();
                setBasicInfo(europeId, desc, homeEntity);
                homeEntity.setTeam(HOME_TEAM_TYPE);
                NowgoalMatchStatisticEntity awayEntity = new NowgoalMatchStatisticEntity();
                setBasicInfo(europeId, desc, awayEntity);
                awayEntity.setTeam(AWAY_TEAM_TYPE);
                String homeCount = ((DomText) tr.getByXPath("td[2]/text()").get(0)).getTextContent();
                homeEntity.setCount(Integer.parseInt(homeCount.replaceAll("%", "")));
                statisticEntities.add(homeEntity);
                String awayCount = ((DomText) tr.getByXPath("td[4]/text()").get(0)).getTextContent();
                awayEntity.setCount(Integer.parseInt(awayCount.replaceAll("%", "")));
                statisticEntities.add(awayEntity);
            }
        }
        return statisticEntities;
    }

    private static void setBasicInfo(int europeId, String desc, NowgoalMatchStatisticEntity homeEntity) {

        homeEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        homeEntity.setItem(desc);
        homeEntity.setMatchId(europeId);
    }
    //------------------------------------------------------------------

    //------------------------------------------------------------------
    private void goPlayersInfo(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalMatchPlayersEntity> players = nowgoalMatchPlayersRepository.findByMatchId((long) europeId);
        if (CollectionUtils.isNotEmpty(players)) {
            return;
        }
        players = getPlayersInfo(europeId, htmlPage);
        for (NowgoalMatchPlayersEntity player : players) {
            nowgoalMatchPlayersRepository.save(player);
        }
    }

    public static List<NowgoalMatchPlayersEntity> getPlayersInfo(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalMatchPlayersEntity> playersEntities = new ArrayList<>();
        List<DomText> homeFirstDivs = (List<DomText>) htmlPage.getByXPath(
                "//div[@class='plays one']/div[@class='home']/div[@class='playBox']/div[@class='play']/div[@class='name']/a/text()");
        List<DomText> awayFirstDivs = (List<DomText>) htmlPage.getByXPath(
                "//div[@class='plays one']/div[@class='guest']/div[@class='playBox']/div[@class='play']/div[@class='name']/a/text()");
        List<DomText> homeSubDivs = (List<DomText>) htmlPage.getByXPath(
                "//div[@class='backupPlay']/div[@class='home']/div[@class='play']/div[@class='name']/a/text()");
        List<DomText> awaySubDivs = (List<DomText>) htmlPage.getByXPath(
                "//div[@class='backupPlay']/div[@class='guest']/div[@class='play']/div[@class='name']/a/text()");
        for (DomText homeFirstDiv : homeFirstDivs) {
            NowgoalMatchPlayersEntity playersEntity = new NowgoalMatchPlayersEntity();
            playersEntity.setTeam(HOME_TEAM_TYPE);
            playersEntity.setMatchId(europeId);
            playersEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            playersEntity.setIsFirst(true);
            playersEntity.setPlayer(homeFirstDiv.getWholeText());
            playersEntities.add(playersEntity);
        }
        for (DomText awayFirstDiv : awayFirstDivs) {
            NowgoalMatchPlayersEntity playersEntity = new NowgoalMatchPlayersEntity();
            playersEntity.setTeam(AWAY_TEAM_TYPE);
            playersEntity.setMatchId(europeId);
            playersEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            playersEntity.setIsFirst(true);
            playersEntity.setPlayer(awayFirstDiv.getWholeText());
            playersEntities.add(playersEntity);
        }
        for (DomText homeSubDiv : homeSubDivs) {
            NowgoalMatchPlayersEntity playersEntity = new NowgoalMatchPlayersEntity();
            playersEntity.setTeam(HOME_TEAM_TYPE);
            playersEntity.setMatchId(europeId);
            playersEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            playersEntity.setIsFirst(false);
            playersEntity.setPlayer(homeSubDiv.getWholeText());
            playersEntities.add(playersEntity);
        }
        for (DomText awaySubDiv : awaySubDivs) {
            NowgoalMatchPlayersEntity playersEntity = new NowgoalMatchPlayersEntity();
            playersEntity.setTeam(AWAY_TEAM_TYPE);
            playersEntity.setMatchId(europeId);
            playersEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            playersEntity.setIsFirst(false);
            playersEntity.setPlayer(awaySubDiv.getWholeText());
            playersEntities.add(playersEntity);
        }
        return playersEntities;
    }

    //------------------------------------------------------------------

    //------------------------------------------------------------------
    private void goMatchInfo(int europeId, HtmlPage htmlPage) throws IOException {

        List<NowgoalMatchEntity> matches = nowgoalMatchRepository.findByEuropeId(europeId);
        if (CollectionUtils.isNotEmpty(matches)) {
            return;
        }
        NowgoalMatchEntity match = getMatchInfo(europeId, htmlPage);
        nowgoalMatchRepository.save(match);
    }

    public static NowgoalMatchEntity getMatchInfo(int europeId, HtmlPage htmlPage) throws IOException {

        NowgoalMatchEntity matchEntity = new NowgoalMatchEntity();
        String home = ((DomText) htmlPage.getByXPath("//div[@id='home']/a/span/text()").get(0)).getWholeText();
        String away = ((DomText) htmlPage.getByXPath("//div[@id='guest']/a/span/text()").get(0)).getWholeText();
        String matchTime = ((DomText) htmlPage.getByXPath(
                "//div[@id='matchItems']/div[@class='item'][2]/span/text()[2]").get(0)).getWholeText();
        matchEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        matchEntity.setHomeTeam(home);
        matchEntity.setAwayTeam(away);
        matchEntity.setMatchTime(matchTime);
        matchEntity.setEuropeId(europeId);

        return matchEntity;
    }
    //------------------------------------------------------------------

    @Override
    public void run() {

        List<TCrawlerWin310> win310s = tCrawlerWin310Repository.findAll();
        for (TCrawlerWin310 win310 : win310s) {
            int europeId = Integer.parseInt(win310.getWin310EuropeId());
            //各个方法相关性不大，一个抛异常不应该影响另一个，而且暂时没有必要拆成多线程，so，每个方法一个try catch
            HtmlPage htmlPage;
            try {
                htmlPage = webClient.getPage("http://www.nowgoal.com/detail/" + europeId + ".html");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } finally {
                webClientJs.closeAllWindows();
            }
            try {
                goKeyEvent(europeId, htmlPage);
            } catch (Exception e) {
                logger.info(ERROR + "key events error, europe id is " + europeId, e);
            }
            try {
                goMatchStatistics(europeId, htmlPage);
            } catch (Exception e) {
                logger.info(ERROR + "match statistics error, europe id is " + europeId, e);
            }
            try {
                goPlayersInfo(europeId, htmlPage);
            } catch (Exception e) {
                logger.info(ERROR + "players info error, europe id is " + europeId, e);
            }
            try {
                goMatchInfo(europeId, htmlPage);
            } catch (Exception e) {
                logger.info(ERROR + "match info error, europe id is " + europeId, e);
            }
        }
    }
}

