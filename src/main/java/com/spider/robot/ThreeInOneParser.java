package com.spider.robot;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.spider.dao.GamingCompanyHadDao;
import com.spider.dao.GamingCompanyHdcDao;
import com.spider.dao.GamingCompanyHiloDao;
import com.spider.domain.GamingCompany;
import com.spider.entity.*;
import com.spider.fetcher.Fetcher;
import com.spider.fetcher.HttpConfig;
import com.spider.fetcher.impl.HttpClientFetcherImpl;
import com.spider.global.ServiceName;
import com.spider.repository.*;
import com.spider.sbc.SbcUpdateManager;
import com.spider.service.HeartBeatService;
import com.spider.utils.LogHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.spider.global.Constants.JINBAOBO_NAME;
import static com.spider.global.Constants.LIJI_NAME;

/**
 * 该类负责利记和金宝博赔率的抓取工作
 * <p/>
 * 页面url：http://data.nowgoal.com/3in1odds/{0}_{1}.html
 * <p/>
 * {0}表示博彩公司在彩客网的id，{@link GamingCompany}
 * {1}表示比赛在彩客网的europeId
 * <p/>
 * 抓到页面后解析出had，hdc，hilo三种玩法的赔率，分别存入had_odds，hhad_odds，highlow_odds表中
 *
 * @author wsy
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThreeInOneParser implements Runnable {

    private HttpConfig httpConfig = new HttpConfig();

    private Logger logger = Logger.getLogger("3in1_logger");

    //    private final String jinbaoboPageUrlTemplate = "http://data.310win.com/changedetail/3in1Odds.aspx?id={0}&companyid=" + GamingCompany.JinBaoBo.getId() + "&l=0";

    /**
     * 金宝博三合一赔率页面，字符串中的参数{0}表示比赛的europeId
     */
    private final String jinbaoboPageUrlTemplate = "http://data.nowgoal.com/3in1odds/" + GamingCompany.JinBaoBo.getId() + "_{0}.html";

    //    private final String lijiPageUrlTemplate = "http://data.310win.com/changedetail/3in1Odds.aspx?id={0}&companyid=" + GamingCompany.LiJi.getId() + "&l=0";

    /**
     * 利记三合一赔率页面，字符串中的参数{0}表示比赛的europeId
     */
    private final String lijiPageUrlTemplate = "http://data.nowgoal.com/3in1odds/" + GamingCompany.LiJi.getId() + "_{0}.html";

    private String companyName;

    @Autowired
    private HadOddsRepository hadOddsRepository;

    @Autowired
    private HiloOddsRepository hiloOddsRepository;

    @Autowired
    private HdcOddsRepository hdcOddsRepository;

    @Autowired
    private TCrawlerWin310Repository win310Repository;

    @Autowired
    private GamingCompanyHiloDao gamingCompanyHiloDao;

    @Autowired
    private GamingCompanyHadDao gamingCompanyHadDao;

    @Autowired
    private GamingCompanyHdcDao gamingCompanyHdcDao;

    @Autowired
    private SbcUpdateManager sbcUpdateManager;

    @Autowired
    private HeartBeatService heartBeatService;

    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    public ThreeInOneParser() {

    }

    public ThreeInOneParser(String companyName) {

        this.companyName = companyName;
    }

    @Override
    public void run() {

        Date start = new Date();
        List<TCrawlerWin310> onSaleMatches = win310Repository.findOnSaleMatches();
        String serviceName = null;
        if (JINBAOBO_NAME.equals(companyName)) {
            serviceName = ServiceName.JinBaoBoRobot.getName();
        } else if (LIJI_NAME.equals(companyName)) {
            serviceName = ServiceName.LiJiRobot.getName();
        }
        try {
            CountDownLatch countDownLatch = new CountDownLatch(onSaleMatches.size());
            for (TCrawlerWin310 win310 : onSaleMatches) {
                executorService.submit(new Runner(win310, countDownLatch));
            }
            countDownLatch.await();
            Date end = new Date();
            heartBeatService.heartBeat(serviceName, start, end, true, null);
        } catch (Exception e) {
            Date end = new Date();
            heartBeatService.heartBeat(serviceName, start, end, false, e.getMessage());
        }
    }

    class Runner implements Runnable {

        private final TCrawlerWin310 win310;

        private final CountDownLatch countDownLatch;

        public Runner(TCrawlerWin310 win310, CountDownLatch countDownLatch) {

            this.countDownLatch = countDownLatch;
            this.win310 = win310;
        }

        @Override
        public void run() {

            parse(win310, countDownLatch);
        }
    }

    public void parse(TCrawlerWin310 win310, CountDownLatch countDownLatch) {

        String url = formatUrl(win310);
        WebClient webClient = new WebClient();
        try {
            LogHelper.info(logger, "url [" + url + "] start....");
            String html = Win310AndSportteryUtils.getOddsHtml(url, new HttpClientFetcherImpl(), httpConfig, null);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage = webClient.getPage(url);
            synchronized (this) {
                Long id = win310.getId();
                Integer europeId = Integer.valueOf(win310.getWin310EuropeId());
                List<HtmlTable> tables = (List<HtmlTable>) htmlPage.getByXPath("//table[@class='gts']");
                if (tables.size() == 0) {
                    LogHelper.info(logger, "url, NO TABLE FOUND");
                    return;
                }
                try {
                    List<HadOddsHistory> hadOddsList = Win310AndSportteryUtils.parseHadOdds(tables.get(0), companyName, europeId);
                    List<HadOdds> queryHadList = hadOddsRepository.findByWin310IdAndGamingCompany(id, companyName);
                    gamingCompanyHadDao.saveOrUpdateHadOdds(queryHadList, hadOddsList, id);
                } catch (Exception e) {
                    LogHelper.error(logger, "parse nowgoal had odds error", e);
                }
                String matchCode = win310.getCompetitionNum();
                try {
                    List<HiloOddsHistory> hiloOddsList = Win310AndSportteryUtils.parseHiloOdds(html, companyName, europeId);
                    List<HiloOdds> queryHiloList = hiloOddsRepository.findByWin310IdAndGamingCompany(id, companyName);
                    HiloOdds updateHiloOdds = gamingCompanyHiloDao.saveOrUpdateHiloOdds(queryHiloList, hiloOddsList, id, companyName);
                    if (updateHiloOdds != null) {
                        sbcUpdateManager.updateSbcHiloOdds(updateHiloOdds, matchCode, companyName);
                        sbcUpdateManager.updateMatchCode(matchCode);
                    }
                } catch (Exception e) {
                    LogHelper.error(logger, "parse nowgoal hilo odds error", e);
                }
                try {
                    List<HdcOddsHistory> hdcOddsList = Win310AndSportteryUtils.parseHdcOdds(html, companyName, europeId);
                    List<HdcOdds> queryHdcList = hdcOddsRepository.findByWin310IdAndGamingCompany(id, companyName);
                    HdcOdds updateHdcOdds = gamingCompanyHdcDao.saveOrUpdateHdcOdds(queryHdcList, hdcOddsList, id, companyName);
                    if (updateHdcOdds != null) {
                        sbcUpdateManager.updateSbcHdcOdds(updateHdcOdds, matchCode, companyName);
                        sbcUpdateManager.updateMatchCode(matchCode);
                    }
                } catch (Exception e) {
                    LogHelper.error(logger, "parse nowgoal hdc odds error", e);
                }
            }
        } catch (Exception e) {
            LogHelper.error(logger, "catch " + companyName + " url " + url + " error", e);
        } finally {
            webClient.closeAllWindows();
            countDownLatch.countDown();
            LogHelper.info(logger, GamingCompany.abbr(companyName) + " CountDownLatch is " + countDownLatch.getCount() + ", matchCode[" + win310.getCompetitionNum() + "]");
        }
    }

    private String formatUrl(TCrawlerWin310 win310) {

        String url = null;
        if (JINBAOBO_NAME.equals(companyName)) {
            url = MessageFormat.format(jinbaoboPageUrlTemplate, win310.getWin310EuropeId());
        } else if (LIJI_NAME.equals(companyName)) {
            url = MessageFormat.format(lijiPageUrlTemplate, win310.getWin310EuropeId());
        }
        return url;
    }
}
