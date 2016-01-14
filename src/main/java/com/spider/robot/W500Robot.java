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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wsy on 2016/1/7.
 *
 * @author wsy
 */
@Component
public class W500Robot implements Runnable {

    public static final String INFO = "[INFO]-";

    public static final String ERROR = "[ERROR]-";

    private static Logger logger = Logger.getLogger("500_logger");

    private WebClient webClient;

    {
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
    }

    private BlockingQueue<HtmlPage> htmlPageBlockingQueue = new LinkedBlockingDeque<>();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private W500Dao w500Dao;

    @Autowired
    private HeartBeatService heartBeatService;

    private String url = "http://live.500.com/?e=";

    @Value("${500.html.file.path}")
    private String pathW500;

    @Override
    public void run() {

        executorService.submit(new FileDealer());
        logger.info(INFO + "start FileDealer...");
        Date start = new Date();
        HtmlPage page1;
        try {
            while ((page1 = htmlPageBlockingQueue.take()) != null) {
                try {
                    //            HtmlPage page1 = webClient.getPage(getUrl(new Date()));
                    //            webClient.setJavaScriptTimeout(5000);
                    List<W500Entity> w500Entities1 = parse(page1);
                    //            HtmlPage page2 = webClient.getPage(getUrl(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))));
                    //            webClient.setJavaScriptTimeout(5000);
                    //            List<W500Entity> w500Entities2 = parse(page2);
                    //            w500Entities1.addAll(w500Entities2);
                    for (W500Entity w500Entity : w500Entities1) {
                        w500Dao.saveOrUpdate(w500Entity);
                    }
                    Date end = new Date();
                    heartBeatService.heartBeat(ServiceName.W500.getName(), start, end, true, null);
                } catch (Exception e) {
                    Date end = new Date();
                    heartBeatService.heartBeat(ServiceName.W500.getName(), start, end, false, e.getMessage());
                } finally {
                    //            webClient.closeAllWindows();
                }
            }
        } catch (InterruptedException e) {
            logger.info(ERROR + "InterruptedException", e);
        }
    }

    public static List<W500Entity> parse(HtmlPage page) {

        HtmlTable table = (HtmlTable) page.getByXPath("//*[@id=\"table_match\"]").get(0);
        List<HtmlTableRow> trs = (List<HtmlTableRow>) table.getByXPath("tbody/tr");
        List<W500Entity> w500Entities = new ArrayList<>();
        for (HtmlTableRow tr : trs) {
            W500Entity w500Entity = new W500Entity();
            try {
                String matchCode = tr.getAttribute("order");
                logger.info(INFO + "start parse W500Entity, match code is " + matchCode);
                w500Entity.setMatchCode(Integer.valueOf(matchCode));
                setTeams(tr, w500Entity);
                setDurationTime(tr, w500Entity);
                setScore(tr, w500Entity);
                setMatchTime(tr, w500Entity);
                w500Entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                setCards(tr, w500Entity);
                setOdds(tr, w500Entity);
                w500Entity.setUniqueId(RobotUtils.getUniqueMatchId(w500Entity.getMatchTime(), w500Entity.getMatchCode() + ""));
                logger.info(INFO + "end parse W500Entity, " + w500Entity);
                w500Entities.add(w500Entity);
            } catch (Exception e) {
                logger.info(ERROR + "parse W500Entity failed", e);
            }
        }
        return w500Entities;
    }

    private static void setOdds(HtmlTableRow tr, W500Entity w500Entity) {

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

    private static void setTeams(HtmlTableRow tr, W500Entity w500Entity) {

        String teams = tr.getAttribute("gy");
        String homeTeam = teams.split(",")[1];
        w500Entity.setHomeTeam(homeTeam);
        String awayTeam = teams.split(",")[2];
        w500Entity.setAwayTeam(awayTeam);
    }

    private static void setDurationTime(HtmlTableRow tr, W500Entity w500Entity) {

        String time = tr.getAttribute("time");
        List<DomText> durationTimeList = (List<DomText>) tr.getByXPath("td[5]/text()");
        String durationTime = "";
        if (durationTimeList.size() != 0) {
            durationTime = durationTimeList.get(0).getWholeText();
        } else {
            durationTimeList = (List<DomText>) tr.getByXPath("td[5]/span/text()");
            if (durationTimeList.size() != 0) {
                durationTime = durationTimeList.get(0).getWholeText();//rtodo
            }
        }
        durationTime = durationTime.replaceAll("\\+", "").replaceAll("'", "");
        try {
            Integer minute = Integer.parseInt(durationTime);
            w500Entity.setDurationTime(durationTime);
            if (minute <= 45) {
                w500Entity.setHalf("上");
            } else if (minute > 45) {
                w500Entity.setHalf("下");
            }
        } catch (Exception e) {
            if (StringUtils.isNotBlank(durationTime)) {
                if (durationTime.contains("未")) {
                    w500Entity.setHalf("未");
                } else if (durationTime.contains("完")) {
                    w500Entity.setHalf("完");
                } else if (durationTime.contains("中")) {
                    w500Entity.setHalf("中");
                } else {
                    w500Entity.setHalf(durationTime);
                }
            }
            w500Entity.setDurationTime("0");
        }
    }

    private static void setMatchTime(HtmlTableRow tr, W500Entity w500Entity) {

        String matchTime = ((DomText) tr.getByXPath("td[4]/text()").get(0)).getWholeText();
        Date startTime = DateUtils.parseDateFormat(Calendar.getInstance().get(Calendar.YEAR) + "-" + matchTime, "yyy-MM-dd HH:mm");
        w500Entity.setMatchTime(new Timestamp(startTime.getTime()));
    }

    private static void setScore(HtmlTableRow tr, W500Entity w500Entity) {

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

    private static void setCards(HtmlTableRow tr, W500Entity w500Entity) {

        Integer homeRedCard = getCardNumber(tr, "td[6]/span[@class='redcard']/text()");
        w500Entity.setHomeRedCard(homeRedCard);
        Integer awayRedCard = getCardNumber(tr, "td[8]/span[@class='redcard']/text()");
        w500Entity.setAwayRedCard(awayRedCard);
        Integer homeYellowCard = getCardNumber(tr, "td[6]/span[@class='yellowcard']/text()");
        w500Entity.setHomeYellowCard(homeYellowCard);
        Integer awayYellowCard = getCardNumber(tr, "td[8]/span[@class='yellowcard']/text()");
        w500Entity.setAwayYellowCard(awayYellowCard);
    }

    private static Integer getCardNumber(HtmlTableRow tr, String xpath) {

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

    class FileDealer implements Runnable {

        @Override
        public void run() {

            while (true) {
                Collection<File> files = FileUtils.listFiles(new File("e:\\500"), new String[]{"html"}, false);
                if (files.size() < 1) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (File file : files) {
                    try {
                        HtmlPage htmlPage = RobotUtils.getHtmlPageFromFile(file, "gbk");
                        htmlPageBlockingQueue.put(htmlPage);
                        logger.info(INFO + "put HtmlPage to queue, filename is " + file.getName());
                        file.delete();
                    } catch (InterruptedException e) {
                        logger.info(ERROR + "InterruptedException, filename is " + file.getName(), e);
                    } catch (IOException e) {
                        logger.info(INFO + "IOException, filename is " + file.getName(), e);
                    }
                }
            }
        }
    }
}
