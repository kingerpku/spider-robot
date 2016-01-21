package com.spider.robot;

import com.gargoylesoftware.htmlunit.html.*;
import com.spider.dao.CompanyOddsDao;
import com.spider.domain.CompanyOddsParam;
import com.spider.domain.GamingCompany;
import com.spider.entity.CompanyOddsEntity;
import com.spider.entity.TCrawlerWin310;
import com.spider.fetcher.Fetcher;
import com.spider.fetcher.HttpConfig;
import com.spider.fetcher.impl.HttpClientFetcherImpl;
import com.spider.global.Constants;
import com.spider.global.ServiceName;
import com.spider.repository.TCrawlerWin310Repository;
import com.spider.sbc.SbcUpdateManager;
import com.spider.service.HeartBeatService;
import com.spider.utils.LogHelper;
import com.spider.utils.RobotUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.spider.global.Constants.JINBAOBO_NAME;
import static com.spider.global.Constants.LIJI_NAME;

/**
 * Created by wsy on 2016/1/8.
 *
 * @author wsy
 */
@Component
public class CompanyOddsRobot implements Runnable {

    private static Logger logger = Logger.getLogger("company_odds_logger");

    private static final String jinbaoboPageUrlTemplate = "http://data.nowgoal.com/3in1odds/" + GamingCompany.JinBaoBo.getId() + "_{0}.html";

    private static final String lijiPageUrlTemplate = "http://data.nowgoal.com/3in1odds/" + GamingCompany.LiJi.getId() + "_{0}.html";

    private String companyName;

    private Fetcher fetcher = new HttpClientFetcherImpl();

    private HttpConfig httpConfig = new HttpConfig();

    @Autowired
    private TCrawlerWin310Repository win310Repository;

    @Autowired
    private HeartBeatService heartBeatService;

    @Autowired
    private CompanyOddsDao companyOddsDao;

    @Autowired
    private SbcUpdateManager sbcUpdateManager;

    private BlockingQueue<CompanyOddsParam> blockingQueue = new LinkedBlockingQueue<>();

    private ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public CompanyOddsRobot() {

    }

    public CompanyOddsRobot(String companyName) {

        this.companyName = companyName;
    }

    @PostConstruct
    public void startParse() {

        singleExecutorService.submit(new Parser());
    }

    @Override
    public void run() {

        Date start = new Date();
        List<TCrawlerWin310> onSaleMatches = win310Repository.findOnSaleMatches();
//        List<TCrawlerWin310> onSaleMatches = win310Repository.findAll();
        String serviceName = null;
        if (JINBAOBO_NAME.equals(companyName)) {
            serviceName = ServiceName.JinBaoBoRobot.getName();
        } else if (LIJI_NAME.equals(companyName)) {
            serviceName = ServiceName.LiJiRobot.getName();
        }
        try {
            CountDownLatch countDownLatch = new CountDownLatch(onSaleMatches.size());
            for (TCrawlerWin310 win310 : onSaleMatches) {
                executorService.submit(new HtmlGetter(win310, countDownLatch));
            }
            countDownLatch.await();
            Date end = new Date();
            heartBeatService.heartBeat(serviceName, start, end, true, null);
        } catch (Exception e) {
            Date end = new Date();
            heartBeatService.heartBeat(serviceName, start, end, false, e.getMessage());
        }
    }

    class HtmlGetter implements Runnable {

        private final TCrawlerWin310 win310;

        private final CountDownLatch countDownLatch;

        public HtmlGetter(TCrawlerWin310 win310, CountDownLatch countDownLatch) {

            this.countDownLatch = countDownLatch;
            this.win310 = win310;
        }

        @Override
        public void run() {

            String url = null;
            Integer europeId = Integer.valueOf(win310.getWin310EuropeId());
            if (JINBAOBO_NAME.equals(companyName)) {
                url = MessageFormat.format(jinbaoboPageUrlTemplate, europeId + "");
            } else if (LIJI_NAME.equals(companyName)) {
                url = MessageFormat.format(lijiPageUrlTemplate, europeId + "");
            }
            try {
                String html = Win310AndSportteryUtils.getOddsHtml(url, new HttpClientFetcherImpl(), httpConfig, Constants.AWS_HTTP_PROXY);
                HtmlPage htmlPage = RobotUtils.getHtmlPageFromString(html, "gbk");
                CompanyOddsParam companyOddsParam = new CompanyOddsParam();
                companyOddsParam.setCompanyName(companyName);
                companyOddsParam.setEuropeId(europeId);
                companyOddsParam.setHtmlPage(htmlPage);
                blockingQueue.put(companyOddsParam);
                LogHelper.info(logger, "put CompanyOddsParam in blocking queue, europe id is " + companyOddsParam.getEuropeId());
            } catch (IOException e) {
                LogHelper.error(logger, "IOException", e);
            } catch (InterruptedException e) {
                LogHelper.error(logger, "InterruptedException", e);
            } catch (Exception e) {
                LogHelper.error(logger, "Exception", e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    class Parser implements Runnable {

        @Override
        public void run() {

            CompanyOddsParam companyOddsParam;
            try {
                while ((companyOddsParam = blockingQueue.take()) != null) {
                    Integer europeId = companyOddsParam.getEuropeId();
                    LogHelper.info(logger, "take CompanyOddsParam from blocking queue, europe id is " + europeId);
                    Map<Integer, List<CompanyOddsEntity>> map = parseOdds(companyName, europeId, companyOddsParam.getHtmlPage());
                    for (Integer key : map.keySet()) {
                        List<CompanyOddsEntity> oddsList = map.get(key);
                        Long id = companyOddsDao.saveOrUpdate(oddsList, key, europeId);
                        if (id != null) {
                            sbcUpdateManager.updateOdds(id);
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.info("[ERROR] InterruptedException " + e);
                executorService.shutdownNow();
                executorService.submit(new Parser());
                LogHelper.info(logger, "restart Parser task");
            }
        }

        public Map<Integer, List<CompanyOddsEntity>> parseOdds(String companyName, Integer europeId, HtmlPage htmlPage) {

            Map<Integer, List<CompanyOddsEntity>> result = new HashMap<>();

            List<HtmlTable> tables;
            try {
                tables = (List<HtmlTable>) htmlPage.getByXPath("//table[@class='gts']");
                if (tables.size() == 0) {
                    LogHelper.info(logger, "(List<HtmlTable>) htmlPage.getByXPath(\"//table[@class='gts']\"), NO TABLE FOUND");
                    return result;
                }
            } catch (Exception e) {
                LogHelper.error(logger, "(List<HtmlTable>) htmlPage.getByXPath(\"//table[@class='gts']\")", e);
                return result;
            }
            for (int i = 0; i < tables.size(); i++) {
                List<CompanyOddsEntity> list = new LinkedList<>();
                List<HtmlTableRow> trs = tables.get(i).getRows();
                for (int j = 2; j < trs.size(); j++) {
                    CompanyOddsEntity odds = new CompanyOddsEntity();
                    HtmlTableRow tr = trs.get(j);
                    String score = ((DomText) tr.getByXPath("td[2]/text()").get(0)).getWholeText();
                    if (score.contains("比分")) {
                        continue;
                    } else {
                        odds.setScore(score);
                    }
                    setRedCards(odds, tr);
                    String durationTime = "";
                    List<?> durationTimeList = tr.getByXPath("td[1]/text()");
                    if (durationTimeList.size() != 0) {
                        durationTime = ((DomText) durationTimeList.get(0)).getWholeText();
                    }
                    String odds1 = getOdds(tr, "td[3]/text()");
                    String odds2 = getOdds(tr, "td[4]/text()");
                    String odds3 = getOdds(tr, "td[5]/text()");
                    String updateTime = ((HtmlScript) tr.getByXPath("td[6]/script").get(0)).getTextContent();
                    String state = ((DomText) tr.getByXPath("td[7]/text()").get(0)).getWholeText();
                    odds.setOddsOne(odds1);
                    odds.setOddsTwo(odds2);
                    odds.setOddsThree(odds3);
                    odds.setDurationTime(durationTime);
                    odds.setOddsUpdateTime(dealNowgoalsUpdateTime(updateTime));
                    odds.setState(state.replaceAll("\\s*", ""));
                    odds.setGamingCompany(companyName);
                    odds.setEuropeId(europeId);
                    odds.setOddsType(i);
                    LogHelper.info(logger, "parse company odds, " + odds);
                    if (odds.valid()) {
                        list.add(odds);
                        break;
                    }
                }
                result.put(i, list);
            }
            return result;
        }

        private void setRedCards(CompanyOddsEntity odds, HtmlTableRow tr) {

            List<?> redCards = tr.getByXPath("td[2]/font");
            if (redCards.size() == 1) {
                HtmlFont cardFont = (HtmlFont) redCards.get(0);
                if (cardFont.getNextSibling() == null) {
                    odds.setAwayRedCard(Integer.valueOf(cardFont.asText()));
                    odds.setHomeRedCard(0);
                } else {
                    odds.setHomeRedCard(Integer.valueOf(cardFont.asText()));
                    odds.setAwayRedCard(0);
                }
            } else if (redCards.size() == 2) {
                odds.setHomeRedCard(Integer.valueOf(((HtmlFont) redCards.get(0)).asText()));
                odds.setAwayRedCard(Integer.valueOf(((HtmlFont) redCards.get(1)).asText()));
            } else {
                odds.setHomeRedCard(0);
                odds.setAwayRedCard(0);
            }
        }

        private String getOdds(HtmlTableRow tr, String xpath) {

            String odds = "";
            List<?> oddsAList = tr.getByXPath(xpath);
            if (oddsAList.size() != 0) {
                odds = ((DomText) oddsAList.get(0)).getWholeText();
            }
            return odds;
        }

        private String dealNowgoalsUpdateTime(String updateTime) {

            updateTime = updateTime.replaceAll("showDate\\(", "").replaceAll("\\)", "");
            return updateTime;
        }
    }
}
