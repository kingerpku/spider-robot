package com.spider.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.spider.dao.SportteryDao;
import com.spider.entity.SportteryAllEntity;
import com.spider.fetcher.Fetcher;
import com.spider.fetcher.HttpConfig;
import com.spider.fetcher.impl.HttpClientFetcherImpl;
import com.spider.global.Constants;
import com.spider.global.ServiceName;
import com.spider.service.HeartBeatService;
import com.spider.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 该类负责用于抓取官网所有玩法的当日比赛赔率信息
 * <p/>
 * 请求地址为：http://info.sporttery.cn/interface/interface_mixed.php?action=fb_list&pke={0}&_={1}
 * <p/>
 * {0}是一个小于1的随机数
 * {1}是当前时间的毫秒数
 * <p/>
 * 返回json格式的数据，解析入库即可，存入表sporttery_all中
 * <p/>
 * 存储策略参考win310{@link Win310Robot}，{@link SportteryDao}
 *
 * @author wsy
 */
@Component
public class SportteryAllRobot implements Runnable {

    private String pageUrl = "http://info.sporttery.cn/interface/interface_mixed.php?action=fb_list&pke={0}&_={1}";

    private Fetcher fetcher = new HttpClientFetcherImpl();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    private static Logger logger = LogHelper.getInfoLogger();

    private HttpConfig httpConfig = new HttpConfig();

    private ScheduledExecutorService scheduleExecutor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private HeartBeatService heartBeatService;

    @Autowired
    private SportteryDao sportteryDao;

    @Override
    public void run() {

        Date start = new Date();
        LogHelper.infoLog(logger, null, this.getClass().getName() + " start...  time:{0}", DateUtils.getDate());
        try {
            pageUrl = MessageFormat.format(pageUrl, new Random().nextInt(1), new Date().getTime());
            String html = Win310AndSportteryUtils.getOddsHtml(pageUrl, fetcher, httpConfig, Constants.AWS_HTTP_PROXY);
            if (StringUtils.isEmpty(html)) {
                LogHelper.errorLog(errorLogger, null, "url:{0},html is empty", pageUrl);
                return;
            }
            List<SportteryAllEntity> list = process(html);
            sportteryDao.compareAndUpdateSportteryAllBeans(list);
            Date end = new Date();
            heartBeatService.heartBeat(ServiceName.SportteryAllRobot.getName(), start, end, true, null);
        } catch (Exception e) {
            Date end = new Date();
            heartBeatService.heartBeat(ServiceName.SportteryAllRobot.getName(), start, end, false, e.getMessage());
            LogHelper.errorLog(errorLogger, e, "", "");
        }
    }

    public List<SportteryAllEntity> process(String jsonArray) throws ParseException {

        List<SportteryAllEntity> list = new ArrayList<>();
        String parsedJson = jsonArray.replaceAll("var\\s+data=", "").replaceAll(";getData\\(\\);", "");
        JSONArray arrayLevel1 = JSON.parseArray(parsedJson);
        for (int i = 0; i < arrayLevel1.size(); i++) {
            SportteryAllEntity sportteryAllEntity = json2Bean(arrayLevel1.getJSONArray(i));
            list.add(sportteryAllEntity);
        }
        return list;
    }

    /**
     * 返回的json数据格式如下：
     * <p/>
     * [ "周四001", "澳大利亚超级联赛", "墨尔本胜利$-1$中央海岸水手", "15-11-19 17:00", "73682", "#FF7000" ],
     * [ "1.88", "3.80", "3.00", "1" ],
     * [ "8.50", "7.25", "7.00", "9.00", "9.50", "17.00", "14.00", "15.00", "30.00", "27.00", "29.00", "50.00", "15.00",
     * "20.00", "9.50", "16.00", "50.00", "250.0",
     * "21.00", "40.00", "18.00", "100.0", "50.00", "40.00", "300.0", "200.0", "150.0", "900.0", "600.0", "500.0", "120.0", "1", "1" ],
     * [ "20.00", "6.50", "4.10", "3.55", "4.25", "6.50", "9.50", "12.00", "1" ],
     * [ "1.82", "17.00", "50.00", "3.80", "7.00", "14.00", "20.00", "17.00", "10.50", "1" ],
     * [ "1.27", "4.95", "7.50", "1" ]
     *
     * @param jsonArray
     * @return
     */
    private SportteryAllEntity json2Bean(JSONArray jsonArray) {

        SportteryAllEntity sportteryAllEntity = new SportteryAllEntity();

        JSONArray basicArr = jsonArray.getJSONArray(0);
        setBasicData(sportteryAllEntity, basicArr);

        JSONArray hhadArr = jsonArray.getJSONArray(1);
        setHhadData(sportteryAllEntity, hhadArr);

        JSONArray scoreArr = jsonArray.getJSONArray(2);
        setScoreData(sportteryAllEntity, scoreArr);

        JSONArray ttgArr = jsonArray.getJSONArray(3);
        setTtgData(sportteryAllEntity, ttgArr);

        JSONArray hafuArr = jsonArray.getJSONArray(4);
        setHafuData(sportteryAllEntity, hafuArr);

        JSONArray hadArr = jsonArray.getJSONArray(5);
        setHadData(sportteryAllEntity, hadArr);

        Long uniqueId = RobotUtils.getUniqueMatchId(sportteryAllEntity.getStartDateTime(), sportteryAllEntity.getMatchCode());
        sportteryAllEntity.setUniqueId(uniqueId);
        return sportteryAllEntity;
    }

    private void setHafuData(SportteryAllEntity sportteryAllEntity, JSONArray hafuArr) {

        sportteryAllEntity.setHafuHh(hafuArr.getDouble(0));
        sportteryAllEntity.setHafuHd(hafuArr.getDouble(1));
        sportteryAllEntity.setHafuHa(hafuArr.getDouble(2));
        sportteryAllEntity.setHafuDh(hafuArr.getDouble(3));
        sportteryAllEntity.setHafuDd(hafuArr.getDouble(4));
        sportteryAllEntity.setHafuDa(hafuArr.getDouble(5));
        sportteryAllEntity.setHafuAh(hafuArr.getDouble(6));
        sportteryAllEntity.setHafuAd(hafuArr.getDouble(7));
        sportteryAllEntity.setHafuAa(hafuArr.getDouble(8));
    }

    private void setHadData(SportteryAllEntity sportteryAllEntity, JSONArray hadArr) {

        sportteryAllEntity.setHadH(hadArr.getDouble(0));
        sportteryAllEntity.setHadD(hadArr.getDouble(1));
        sportteryAllEntity.setHadA(hadArr.getDouble(2));
    }

    private void setTtgData(SportteryAllEntity sportteryAllEntity, JSONArray ttgArr) {

        sportteryAllEntity.setTtg0(ttgArr.getDouble(0));
        sportteryAllEntity.setTtg1(ttgArr.getDouble(1));
        sportteryAllEntity.setTtg2(ttgArr.getDouble(2));
        sportteryAllEntity.setTtg3(ttgArr.getDouble(3));
        sportteryAllEntity.setTtg4(ttgArr.getDouble(4));
        sportteryAllEntity.setTtg5(ttgArr.getDouble(5));
        sportteryAllEntity.setTtg6(ttgArr.getDouble(6));
        sportteryAllEntity.setTtg7Up(ttgArr.getDouble(7));
    }

    private void setScoreData(SportteryAllEntity sportteryAllEntity, JSONArray scoreArr) {

        sportteryAllEntity.setScore10(scoreArr.getDouble(0));
        sportteryAllEntity.setScore20(scoreArr.getDouble(1));
        sportteryAllEntity.setScore21(scoreArr.getDouble(2));
        sportteryAllEntity.setScore30(scoreArr.getDouble(3));
        sportteryAllEntity.setScore31(scoreArr.getDouble(4));
        sportteryAllEntity.setScore32(scoreArr.getDouble(5));
        sportteryAllEntity.setScore40(scoreArr.getDouble(6));
        sportteryAllEntity.setScore41(scoreArr.getDouble(7));
        sportteryAllEntity.setScore42(scoreArr.getDouble(8));
        sportteryAllEntity.setScore50(scoreArr.getDouble(9));
        sportteryAllEntity.setScore51(scoreArr.getDouble(10));
        sportteryAllEntity.setScore52(scoreArr.getDouble(11));
        sportteryAllEntity.setScoreHElse(scoreArr.getDouble(12));
        sportteryAllEntity.setScore00(scoreArr.getDouble(13));
        sportteryAllEntity.setScore11(scoreArr.getDouble(14));
        sportteryAllEntity.setScore22(scoreArr.getDouble(15));
        sportteryAllEntity.setScore33(scoreArr.getDouble(16));
        sportteryAllEntity.setScoreDElse(scoreArr.getDouble(17));
        sportteryAllEntity.setScore01(scoreArr.getDouble(18));
        sportteryAllEntity.setScore02(scoreArr.getDouble(19));
        sportteryAllEntity.setScore12(scoreArr.getDouble(20));
        sportteryAllEntity.setScore03(scoreArr.getDouble(21));
        sportteryAllEntity.setScore13(scoreArr.getDouble(22));
        sportteryAllEntity.setScore23(scoreArr.getDouble(23));
        sportteryAllEntity.setScore04(scoreArr.getDouble(24));
        sportteryAllEntity.setScore14(scoreArr.getDouble(25));
        sportteryAllEntity.setScore24(scoreArr.getDouble(26));
        sportteryAllEntity.setScore05(scoreArr.getDouble(27));
        sportteryAllEntity.setScore15(scoreArr.getDouble(28));
        sportteryAllEntity.setScore25(scoreArr.getDouble(29));
        sportteryAllEntity.setScoreAElse(scoreArr.getDouble(30));
    }

    private void setHhadData(SportteryAllEntity sportteryAllEntity, JSONArray hhadArr) {

        sportteryAllEntity.setHhadH(hhadArr.getDouble(0));
        sportteryAllEntity.setHhadD(hhadArr.getDouble(1));
        sportteryAllEntity.setHhadA(hhadArr.getDouble(2));
    }

    private void setBasicData(SportteryAllEntity sportteryAllEntity, JSONArray basicArr) {

        sportteryAllEntity.setMatchCode(Win310AndSportteryUtils.transSportteryMatchCode(basicArr.getString(0)));
        sportteryAllEntity.setLeague(basicArr.getString(1));
        String[] teamArr = basicArr.getString(2).split("\\$");
        sportteryAllEntity.setHomeTeam(teamArr[0]);
        sportteryAllEntity.setAwayTeam(teamArr[2]);
        sportteryAllEntity.setHhadLine(DoubleUtils.parse(teamArr[1], 0.0));
        sportteryAllEntity.setStartDateTime(DateUtils.fromFormatStr(basicArr.getString(3), "yy-MM-dd HH:mm"));
    }

}
