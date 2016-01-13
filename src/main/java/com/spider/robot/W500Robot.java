package com.spider.robot;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.spider.dao.W500Dao;
import com.spider.entity.W500Entity;
import com.spider.global.ServiceName;
import com.spider.service.HeartBeatService;
import com.spider.utils.DateUtils;
import com.spider.utils.RobotUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wsy on 2016/1/7.
 *
 * @author wsy
 */
@Component
public class W500Robot implements Runnable {

    private WebClient webClient;

    {
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
    }

    @Autowired
    private W500Dao w500Dao;

    @Autowired
    private HeartBeatService heartBeatService;

    private String url = "http://live.500.com/?e=";

    @Override
    public void run() {

        Date start = new Date();
        try {
            webClient.setJavaScriptTimeout(3000);
            HtmlPage page1 = webClient.getPage(getUrl(new Date()));
            parse(page1);
            HtmlPage page2 = webClient.getPage(getUrl(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))));
            parse(page2);
            Date end = new Date();
            heartBeatService.heartBeat(ServiceName.W500.getName(), start, end, true, null);
        } catch (Exception e) {
            Date end = new Date();
            heartBeatService.heartBeat(ServiceName.W500.getName(), start, end, false, e.getMessage());
        } finally {
            webClient.closeAllWindows();
        }
    }

    private void parse(HtmlPage page) {

        HtmlTable table = (HtmlTable) page.getByXPath("//*[@id=\"table_match\"]").get(0);
        List<HtmlTableRow> trs = (List<HtmlTableRow>) table.getByXPath("tbody/tr");
        for (HtmlTableRow tr : trs) {
            W500Entity w500Entity = new W500Entity();
            String matchCode = tr.getAttribute("order");
            w500Entity.setMatchCode(Integer.valueOf(matchCode));
            setTeams(tr, w500Entity);
            setDurationTime(tr, w500Entity);
            setScore(tr, w500Entity);
            setMatchTime(tr, w500Entity);
            w500Entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            setCards(tr, w500Entity);
            setOdds(tr, w500Entity);
            w500Entity.setUniqueId(RobotUtils.getUniqueMatchId(w500Entity.getMatchTime(), w500Entity.getMatchCode() + ""));
            w500Dao.saveOrUpdate(w500Entity);
        }
    }

    private void setOdds(HtmlTableRow tr, W500Entity w500Entity) {

        String hadH = ((DomText) tr.getByXPath("td[10]/span[1]/text()").get(0)).getWholeText();
        String hadD = ((DomText) tr.getByXPath("td[10]/span[2]/text()").get(0)).getWholeText();
        String hadA = ((DomText) tr.getByXPath("td[10]/span[3]/text()").get(0)).getWholeText();
        try {
            w500Entity.setHadH(Double.valueOf(hadH));
            w500Entity.setHadD(Double.valueOf(hadD));
            w500Entity.setHadA(Double.valueOf(hadA));
        } catch (Exception e) {
            w500Entity.setHadH(0.0);
            w500Entity.setHadD(0.0);
            w500Entity.setHadA(0.0);
        }
    }

    private void setTeams(HtmlTableRow tr, W500Entity w500Entity) {

        String teams = tr.getAttribute("gy");
        String homeTeam = teams.split(",")[1];
        w500Entity.setHomeTeam(homeTeam);
        String awayTeam = teams.split(",")[2];
        w500Entity.setAwayTeam(awayTeam);
    }

    private void setDurationTime(HtmlTableRow tr, W500Entity w500Entity) {

        String time = tr.getAttribute("time");
        List<DomText> durationTimeList = (List<DomText>) tr.getByXPath("td[5]/text()");
        String durationTime = "";
        String wholeText = "";
        if (durationTimeList.size() != 0) {
            wholeText = durationTimeList.get(0).getWholeText();
        }
        if (wholeText.equals("") || (!wholeText.contains("未") && !wholeText.matches(".*\\d.*"))) {
            durationTimeList = (List<DomText>) tr.getByXPath("td[5]/span/text()");
            if (durationTimeList.size() != 0) {
                durationTime = durationTimeList.get(0).getWholeText();//rtodo
            }
        } else {
            durationTime = wholeText;
        }
        w500Entity.setDurationTime(durationTime);
    }

    private void setMatchTime(HtmlTableRow tr, W500Entity w500Entity) {

        String matchTime = ((DomText) tr.getByXPath("td[4]/text()").get(0)).getWholeText();
        Date startTime = DateUtils.parseDateFormat(Calendar.getInstance().get(Calendar.YEAR) + "-" + matchTime, "yyy-MM-dd HH:mm");
        w500Entity.setMatchTime(new Timestamp(startTime.getTime()));
    }

    private void setScore(HtmlTableRow tr, W500Entity w500Entity) {

        List<DomText> scoreHomeList = (List<DomText>) tr.getByXPath("td[7]/div/a[1]/text()");
        String scoreHome;
        if (scoreHomeList.size() == 0) {
            scoreHome = "";
        } else {
            scoreHome = scoreHomeList.get(0).getWholeText();
        }
        List<DomText> scoreAwayList = (List<DomText>) tr.getByXPath("td[7]/div/a[3]/text()");
        String scoreAway;
        if (scoreAwayList.size() == 0) {
            scoreAway = "";
        } else {
            scoreAway = scoreAwayList.get(0).getWholeText();
        }
        String score = scoreHome + "-" + scoreAway;
        w500Entity.setScore(score);
        String halfScore = ((DomText) tr.getByXPath("td[9]/text()").get(0)).getWholeText();
        w500Entity.setHalfScore(halfScore.replaceAll("\\s", ""));
    }

    private void setCards(HtmlTableRow tr, W500Entity w500Entity) {

        Integer homeRedCard = getCardNumber(tr, "td[6]/span[@class='redcard']/text()");
        w500Entity.setHomeRedCard(homeRedCard);
        Integer awayRedCard = getCardNumber(tr, "td[8]/span[@class='redcard']/text()");
        w500Entity.setAwayRedCard(awayRedCard);
        Integer homeYellowCard = getCardNumber(tr, "td[6]/span[@class='yellowcard']/text()");
        w500Entity.setHomeYellowCard(homeYellowCard);
        Integer awayYellowCard = getCardNumber(tr, "td[8]/span[@class='yellowcard']/text()");
        w500Entity.setAwayYellowCard(awayYellowCard);
    }

    private Integer getCardNumber(HtmlTableRow tr, String xpath) {

        List<DomText> cardList = (List<DomText>) tr.getByXPath(xpath);
        Integer homeRedCard;
        if (cardList.size() == 0) {
            homeRedCard = 0;
        } else {
            homeRedCard = Integer.valueOf(cardList.get(0).getWholeText());
        }
        return homeRedCard;
    }

    private String getUrl(Date date) {

        return url + DateUtils.format(date, "yyyy-MM-dd");
    }
}
