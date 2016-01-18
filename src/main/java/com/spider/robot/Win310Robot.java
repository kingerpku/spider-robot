package com.spider.robot;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.spider.entity.TCrawlerWin310;
import com.spider.entity.TCrawlerWin310History;
import com.spider.global.ServiceName;
import com.spider.repository.TCrawlerWin310HistoryRepository;
import com.spider.repository.TCrawlerWin310Repository;
import com.spider.robot.parser.Win310Parser;
import com.spider.service.HeartBeatService;
import com.spider.utils.MyTypeConvert;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spider.global.Constants;
import com.spider.domain.Win310OriginalData;
import com.spider.fetcher.Fetcher;
import com.spider.fetcher.FetcherContent;
import com.spider.fetcher.HttpConfig;
import com.spider.fetcher.impl.HttpClientFetcherImpl;
import com.spider.selector.Selector;
import com.spider.selector.XpathSelector;
import com.spider.utils.LogHelper;

/**
 * 该类用于抓取彩客网数据，并进行解析，需要抓取当日比赛的串关页面和单关页面
 * URL分别为
 * 串关：http://www.310win.com/buy/JingCai2.aspx?TypeID=105&OddsType=2
 * 单关：http://www.310win.com/buy/DanGuan.aspx?TypeID=105&OddsType=1
 * <p/>
 * 存储策略：
 * 解析结果为多场比赛的赛事信息和赔率信息，存入t_crawler_win310表中
 * 表中存储的都是当前在售的比赛，根据match_code字段区分，match_code有唯一约束，不可重复
 * 每日新抓取的比赛根据match_code进行覆盖，对于上周存在而本周不存在的比赛，保留历史数据
 * <p/>
 * 该类负责两类数据的抓取任务
 * 彩客网数据的抓取以及利记，金宝博等博彩公司赔率的抓取
 * 如此设计是因为利记和金宝博的赔率需要从彩客的比赛提供的信息才能获取到{@see ThreeInOneParser}
 * <p/>
 * 抓取原理为循环执行定时任务，每个任务一个线程，由{@link ScheduledExecutorService} 负责调度
 *
 * @author wsy
 */
@Component
public class Win310Robot implements Runnable {

    @Autowired
    private Win310Parser win310Parser;

    private Logger logger = Logger.getLogger("win310_logger");

    private static Logger messLogger = LogHelper.getMessLogger();

    private HttpConfig httpConfig = new HttpConfig();

    private Fetcher fetcher = new HttpClientFetcherImpl();

    @Autowired
    private TCrawlerWin310Repository win310Repository;

    @Autowired
    private TCrawlerWin310HistoryRepository win310HistoryRepository;

    @Autowired
    private HeartBeatService heartBeatService;

    /**
     * 彩客网竞彩足球过关在售比赛页面
     */
    private String pageUrl = "http://www.310win.com/buy/JingCai2.aspx?TypeID=105&OddsType=2";

    @Override
    public void run() {

        Date start = new Date();
        try {
            logger.info("win310 start...");
            FetcherContent fetcherContent = getFetcherContent(pageUrl);
            if (!(fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK)) {
                return;
            }
            String html = fetcherContent.getHtml();
            LogHelper.infoLog(messLogger, null, "win310 fetcher html status_code:{0}", fetcherContent.getStatusCode());
            if (html == null || StringUtils.isEmpty(html.replaceAll(Constants.NBSP_HTML, ""))) {
                LogHelper.errorLog(logger, null, "win310 main html is null...", "");
                Date end = new Date();
                heartBeatService.heartBeat(ServiceName.Win310Robot.getName(), start, end, false, "win310 main html is null...");
                return;
            }
            List<TCrawlerWin310> onSales = parse(html);
            List<TCrawlerWin310> notCompleted = win310Repository.findNotCompletedMatches();
            completeResult(onSales, notCompleted);
            Date end = new Date();
            heartBeatService.heartBeat(ServiceName.Win310Robot.getName(), start, end, true, null);
        } catch (Exception e) {
            Date end = new Date();
            heartBeatService.heartBeat(ServiceName.Win310Robot.getName(), start, end, false, e.getMessage());
        }
    }

    private List<TCrawlerWin310> parse(String html) {

        Selector niDateSelector = new XpathSelector("//div[@id=new_buy_main]/div[@class=td_div]/table/tbody/tr[@class=niDate]/td[1]/text()");
        //比赛日期的集合 例如： 2015年06月03日 星期三 (11：00--次日11：00)  
        List<String> niDates = niDateSelector.selectList(html);

        List<TCrawlerWin310> list = new ArrayList<>();
        try {
            for (String str : niDates) {
                // 去除括号内容，及前后空格 例如：2015年06月03日 星期三
                str = str.replaceAll("\\(.*\\)", "").replaceAll(Constants.NBSP_HTML, "").trim();
                // 将字符串分割为数组 例如： 2015年06月03日 星期三
                String[] arr = str.split("星");

                if (arr == null || arr.length < 2) {
                    continue;
                }
                // 将星期换为周 例如 周三
                String className = arr[1].substring(0, 2).replace("期", "周").replaceAll("[\\s]+", "");
                if (StringUtils.isEmpty(className)) {
                    continue;
                }
                Selector trsSelector = new XpathSelector("//tr[@name=" + className + "]");
                Selector yesterdayTrsSelector = new XpathSelector("//tr[@name=" + Win310AndSportteryUtils.getBeforeWeek(className) + "]");
                // 获取每一天的比赛信息集合 通过className来解析，有<tr
                // id="row_1402005" name="周三" .........>
                List<String> trs = trsSelector.selectList(html);
                // 昨天的比赛今天会写今天的日期，但是className还是写的昨天的日期，所以通过今天得到的className得不到昨天的比赛，所以昨天的还要重新获取一下
                List<String> yesterdayTrs = yesterdayTrsSelector.selectList(html);
                trs.addAll(yesterdayTrs);
                Set<String> singleMatchSet = getSingleSet();
                for (String tr : trs) {
                    Win310OriginalData htmlData = createOriginalData(tr);
                    htmlData.setSingleMatchSet(singleMatchSet);
                    if (htmlData != null) {
                        TCrawlerWin310 win310 = win310Parser.process(htmlData);
                        list.add(win310);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            LogHelper.error(logger, "parser html error", e);
        }
        return list;
    }

    private Win310OriginalData createOriginalData(String tr) {


        String trHtml = "<html><body><table id='xxx'><tbody>" + tr + "</tbody></table></body></html>";
        Selector europeLinkeSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[16]/a[2]/@href");
        String europeLink = europeLinkeSelector.select(trHtml);
        if (StringUtils.isBlank(europeLink)) {
            return null;
        }
        if (!europeLink.startsWith("http://")) {
            europeLink = "http://www.310win.com" + europeLink;
        }
        String win310EuropeId = Win310AndSportteryUtils.getWin310EuropeId(europeLink);
        Win310OriginalData htmlData = new Win310OriginalData();
        htmlData.setMainLink(pageUrl);
        htmlData.setMainContent(trHtml);
        htmlData.setWin310EuropeId(win310EuropeId);
        return htmlData;
    }

    public Set<String> getSingleSet() {

        String singlePageUrl = "http://www.310win.com/buy/DanGuan.aspx?TypeID=105&OddsType=1";
        Set<String> singleSet = new HashSet<>();
        FetcherContent fetcherContent = getFetcherContent(singlePageUrl);
        if (!(fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK)) {
            return singleSet;
        }
        String html = fetcherContent.getHtml();
        LogHelper.infoLog(messLogger, null, "win310 fetcher html status_code:{0}", fetcherContent.getStatusCode());
        if (html == null) {
            LogHelper.error(logger, "win310 main html is null...", null);
            return singleSet;
        }
        if (StringUtils.isEmpty(html.replaceAll(Constants.NBSP_HTML, ""))) {
            return singleSet;
        }
        Selector matchDateSelector = new XpathSelector("//div[@id=new_buy_main]/div/div[@class=td_div]/div[@id=lottery_container]/table[@id=MatchTable]/tbody/tr[@class=niDate]/td[1]/text()");
        List<String> matchDates = matchDateSelector.selectList(html);
        try {
            for (String str : matchDates) {
                str = str.replaceAll("\\(.*\\)", "").replaceAll(Constants.NBSP_HTML, "").trim();
                String[] arr = str.split("星");
                if (arr == null || arr.length < 2) {
                    continue;
                }
                String className = arr[1].substring(0, 2).replace("期", "周").replaceAll("[\\s]+", "");
                if (StringUtils.isEmpty(className)) {
                    continue;
                }
                Selector trsSelector = new XpathSelector("//tr[@name=" + className + "]");
                Selector yesterdayTrsSelector = new XpathSelector("//tr[@name=" + Win310AndSportteryUtils.getBeforeWeek(className) + "]");
                List<String> trs = trsSelector.selectList(html);
                List<String> yesterdayTrs = yesterdayTrsSelector.selectList(html);
                trs.addAll(yesterdayTrs);
                for (String tr : trs) {
                    String trHtml = "<html><body><table id='xxx'><tbody>" + tr + "</tbody></table></body></html>";
                    Selector weekSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/@name");
                    Selector competitionNumSelector = new XpathSelector("//table[@id=xxx]/tbody/tr/td[1]/text()");
                    singleSet.add(Win310AndSportteryUtils.getCompetitionNum(MyTypeConvert.obj2str(competitionNumSelector.select(trHtml)), weekSelector.select(trHtml)));
                }
            }
        } catch (Exception e) {
            LogHelper.error(logger, "parser html error", e);
        }
        return singleSet;
    }

    private String getWin310MatchDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar11 = Calendar.getInstance();
        calendar11.setTime(date);
        calendar11.set(Calendar.HOUR_OF_DAY, 11);
        calendar11.set(Calendar.MINUTE, 0);
        if (calendar.before(calendar11)) {
            calendar.setTime(new Date(date.getTime() - TimeUnit.DAYS.toMillis(1)));
            String result = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            return result;
        } else {
            String result = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            return result;
        }
    }

    /**
     * 对于比赛踢完没有抓到完场的比赛，查询彩客历史记录，补全赛果
     *
     * @param onSales
     * @param notCompleted
     */
    private void completeResult(List<TCrawlerWin310> onSales, List<TCrawlerWin310> notCompleted) {

        List<TCrawlerWin310> needComplete = new ArrayList<>();
        for (TCrawlerWin310 not : notCompleted) {
            if (!inOnSales(not, onSales)) {
                needComplete.add(not);
            }
        }
        for (TCrawlerWin310 win310 : needComplete) {
            if (win310.getStartDateTime().before(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)))) {
                doComplete(win310);
            }
        }
    }

    private boolean inOnSales(TCrawlerWin310 not, List<TCrawlerWin310> onSales) {

        for (TCrawlerWin310 onSale : onSales) {
            if (not.getWin310EuropeId().equals(onSale.getWin310EuropeId())) {
                return true;
            }
        }
        return false;
    }

    private void doComplete(TCrawlerWin310 win310) {

        Date date = win310.getStartDateTime();
        String matchCode = win310.getCompetitionNum();
        String scoreAndState = getScoreAndState(getWin310MatchDate(date), matchCode);
        win310.setScore(scoreAndState.split("\\|")[0]);
        win310.setDurationTime(scoreAndState.split("\\|")[1]);
        win310Repository.save(win310);
        win310HistoryRepository.save(new TCrawlerWin310History().from(win310));
    }

    private static String getScoreAndState(String date, String matchCode) {

        String url = "http://www.310win.com/buy/jingcai.aspx?typeID=105&oddstype=2&date=" + date;
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            HtmlPage htmlPage = webClient.getPage(url);
            List<HtmlTableRow> trs = (List<HtmlTableRow>) htmlPage.getByXPath("//table[@id='MatchTable']/tbody/tr");
            for (HtmlTableRow tr : trs) {
                if (matchCode.substring(1).equals(((DomText) tr.getByXPath("td[1]/text()").get(0)).getWholeText())) {
                    String score = ((DomText) tr.getByXPath("td[7]/text()").get(0)).getWholeText();
                    String matchState;
                    try {
                        matchState = ((DomText) tr.getByXPath("td[4]/font/text()").get(0)).getWholeText();
                    } catch (Exception e) {
                        matchState = ((DomText) tr.getByXPath("td[4]/text()").get(0)).getWholeText();
                    }
                    return score + "|" + matchState;
                }
            }
        } catch (IOException e) {
            System.out.println(matchCode);
            e.printStackTrace();
        } finally {
            webClient.closeAllWindows();
        }
        return "";
    }


    private FetcherContent getFetcherContent(String url) {

        FetcherContent fetcherContent = null;
        for (int i = 0; i < 3; i++) {
            fetcherContent = fetcher.wgetHtml(url, httpConfig, Constants.AWS_HTTP_PROXY);
            if (fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK) {
                break;
            }
        }
        return fetcherContent;
    }

}
