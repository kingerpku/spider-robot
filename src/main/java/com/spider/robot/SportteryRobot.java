package com.spider.robot;

import com.google.common.collect.Lists;
import com.spider.dao.SportteryDao;
import com.spider.entity.TCrawlerSporttery;
import com.spider.global.Constants;
import com.spider.global.ServiceName;
import com.spider.service.HeartBeatService;
import com.spider.utils.MyTypeConvert;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spider.fetcher.Fetcher;
import com.spider.fetcher.FetcherContent;
import com.spider.fetcher.HttpConfig;
import com.spider.fetcher.impl.HttpClientFetcherImpl;
import com.spider.utils.DateUtils;
import com.spider.utils.LogHelper;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 该类负责用于抓取官网had,hhad玩法的当日比赛赔率信息
 * <p/>
 * 请求地址为：http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&poolcode[]=hhad&poolcode[]=had
 * <p/>
 * 返回json格式的数据，解析入库即可，存入表t_crawler_sporttery中
 * <p/>
 * 存储策略参考{@link Win310Robot}，{@link SportteryDao}
 *
 * @author wsy
 */
@Component
public class SportteryRobot {

    private String pageUrl = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&poolcode[]=hhad&poolcode[]=had";

    private Fetcher fetcher = new HttpClientFetcherImpl();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    private static Logger infoLogger = LogHelper.getInfoLogger();

    private HttpConfig httpConfig = new HttpConfig();

    private ScheduledExecutorService scheduleExecutor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private HeartBeatService heartBeatService;

    @Autowired
    private SportteryDao sportteryDao;

    @PostConstruct
    public void post() {

        scheduleExecutor.scheduleAtFixedRate(new Runner(), 5, 30, TimeUnit.SECONDS);
    }

    public class Runner implements Runnable {

        @Override
        public void run() {

            Date start = new Date();
            LogHelper.infoLog(infoLogger, null, this.getClass().getName() + " start...  time:{0}", DateUtils.getDate());
            try {
                String html = getHtml();
                if (StringUtils.isEmpty(html)) {
                    LogHelper.errorLog(errorLogger, null, "url:{0},html is empty", pageUrl);
                    return;
                }
                process(html);
                Date end = new Date();
                heartBeatService.heartBeat(ServiceName.SportteryRobot.getName(), start, end, true, null);
            } catch (Exception e) {
                Date end = new Date();
                heartBeatService.heartBeat(ServiceName.SportteryRobot.getName(), start, end, false, e.getMessage());
                LogHelper.errorLog(errorLogger, e, "", "");
            }
        }

        public String getHtml() {

            FetcherContent fetcherContent = null;
            String html = null;
            for (int i = 0; i < httpConfig.getRetryTimes(); i++) {
                fetcherContent = fetcher.wgetHtml(pageUrl, httpConfig, Constants.AWS_HTTP_PROXY);
                if (fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK) {
                    break;
                } else if (fetcherContent != null && (fetcherContent.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY || fetcherContent.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)) {
                    fetcherContent.setUrl(fetcherContent.getRedirectUrl());
                }
            }
            if (fetcherContent != null && fetcherContent.getStatusCode() == HttpStatus.SC_OK) {
                html = fetcherContent.getHtml();
                if (html == null) {
                    throw new NullPointerException("get html time out");
                }
            }
            LogHelper.infoLog(infoLogger, null, "sporttery fetcher html status_code:{0}", fetcherContent.getStatusCode());
            return html;
        }

        public void process(String jsonArray) throws ParseException {

            String lastUpdated = "";
            if (StringUtils.isEmpty(jsonArray)) {
                return;
            }
            JSONObject myJsonObject = JSONObject.fromObject(jsonArray);
            JSONObject statusJsonObject = myJsonObject.getJSONObject("status");
            if (statusJsonObject != null) {
                lastUpdated = statusJsonObject.getString("last_updated");
            }
            JSONObject dataJsonObject = myJsonObject.getJSONObject("data");
            if (dataJsonObject == null) {
                infoLogger.info("parse data is null");
                return;
            }
            @SuppressWarnings("unchecked")
            Set<String> ids = dataJsonObject.keySet();
            if (CollectionUtils.isEmpty(ids)) {
                infoLogger.info("json object is empty");
                return;
            }
            List<TCrawlerSporttery> list = Lists.newArrayList();
            for (String id : ids) {
                TCrawlerSporttery bean = json2Bean(dataJsonObject.getJSONObject(id));
                if (bean == null) {
                    continue;
                }
                bean.setLastUpdated(org.apache.commons.lang3.time.DateUtils.parseDate(lastUpdated, "yyyy-MM-dd HH:mm:ss"));
                if (bean.validate()) {
                    list.add(bean);
                } else {
                    LogHelper.errorLog(errorLogger, null, "sporttery bean validate error, ", bean.toString());
                }
            }
            sportteryDao.compareAndUpdateBeans(list);
        }

        private TCrawlerSporttery json2Bean(JSONObject jsonObject) {

            TCrawlerSporttery bean = new TCrawlerSporttery();
            String temp;
            try {
                bean.setCompetitionNum(Win310AndSportteryUtils.transSportteryMatchCode(MyTypeConvert.obj2str(jsonObject.get("num"))));
                bean.setStartDateTime(DateUtils.parseDateFormat(MyTypeConvert.obj2str(jsonObject.get("date")) + " " + MyTypeConvert.obj2str(jsonObject.get("time"))));
                bean.setBDate(MyTypeConvert.obj2str(jsonObject.get("date")));
                bean.setHomeTeam(MyTypeConvert.obj2str(jsonObject.get("h_cn_abbr")));
                bean.setVisitionTeam(MyTypeConvert.obj2str(jsonObject.get("a_cn_abbr")));
                bean.setStatus(StringUtils.upperCase(MyTypeConvert.obj2str(jsonObject.get("status")).toLowerCase()));
                bean.setMatchs(MyTypeConvert.obj2str(jsonObject.get("l_cn_abbr")));
                if (jsonObject.containsKey("had")) {
                    JSONObject hadJsonObject = jsonObject.getJSONObject("had");
                    temp = MyTypeConvert.obj2str(hadJsonObject.get("fixedodds"));
                    if ("+1".equals(temp)) {
                        temp = "1";
                    } else if ("".equals(temp)) {
                        temp = "0";
                    }
                    bean.setWinCountOne(Integer.parseInt(temp));
                    bean.setStatusOne(StringUtils.upperCase(MyTypeConvert.obj2str(hadJsonObject.get("p_status"))));
                    bean.setFailProbabilityOne(MyTypeConvert.obj2str(hadJsonObject.get("a")));
                    bean.setDrawProbabilityOne(MyTypeConvert.obj2str(hadJsonObject.get("d")));
                    bean.setWinProbabilityOne(MyTypeConvert.obj2str(hadJsonObject.get("h")));
                }
                if (jsonObject.containsKey("hhad")) {
                    JSONObject hhadJSONObject = jsonObject.getJSONObject("hhad");
                    temp = MyTypeConvert.obj2str(hhadJSONObject.get("fixedodds"));
                    if ("+1".equals(temp)) {
                        temp = "1";
                    } else if ("".equals(temp)) {
                        temp = "0";
                    }
                    bean.setWinCountTwo(Integer.parseInt(temp));
                    bean.setStatusTwo(StringUtils.upperCase(MyTypeConvert.obj2str(hhadJSONObject.get("p_status"))));
                    bean.setFailProbabilityTwo(MyTypeConvert.obj2str(hhadJSONObject.get("a")));
                    bean.setDrawProbabilityTwo(MyTypeConvert.obj2str(hhadJSONObject.get("d")));
                    bean.setWinProbabilityTwo(MyTypeConvert.obj2str(hhadJSONObject.get("h")));
                }
            } catch (Exception e) {
                LogHelper.errorLog(errorLogger, e, "parser json object error,jsonObject:{0}", jsonObject.toString());
                bean = null;
            }
            return bean;
        }
    }

}
