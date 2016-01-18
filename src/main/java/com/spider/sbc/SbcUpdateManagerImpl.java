package com.spider.sbc;

import com.spider.entity.HdcOdds;
import com.spider.entity.HiloOdds;
import com.spider.entity.TCrawlerWin310;
import com.spider.global.Constants;
import com.spider.global.UpdateSBCType;
import com.spider.utils.LogHelper;
import com.spider.utils.MessageSender;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by wsy on 2015/10/13.
 */
@Component
public class SbcUpdateManagerImpl implements SbcUpdateManager {

    private Logger infoLogger = LogHelper.getInfoLogger();

    @Value("${inplay.odds.group}")
    private String producerGroup;

    @Value("${inplay.odds.topic}")
    private String inplayTopic;

    @Value("${inplay.odds.hdc.tag}")
    private String hdcTag;

    @Value("${inplay.odds.hilo.tag}")
    private String hiloTag;

    @Value("${inplay.odds.matlab.tag}")
    private String matlabTag;

    @Value("${inplay.odds.score_half.tag}")
    private String scoreAndHalfTag;

    @Autowired
    private MessageSender messageSender;

    @Override
    public synchronized <T> void update(T data, String tag) {

        messageSender.sendObjectMessage(data, producerGroup, inplayTopic, tag);
    }

    @Override
    public synchronized <T> void update(T data, String topic, String tag) {

        messageSender.sendObjectMessage(data, producerGroup, topic, tag);
    }

    @Override
    public void updateSbcScoreAndHalf(TCrawlerWin310 win310, String matchCode) {

        run(matchCode, UpdateSBCType.ScoreAndHalf.intValue());
    }

    @Override
    public void updateSbcHiloOdds(HiloOdds hiloOdds, String matchCode, String companyName) {

        run(matchCode, UpdateSBCType.HiloOdds.intValue());
    }

    @Override
    public void updateSbcHdcOdds(HdcOdds hdcOdds, String matchCode, String companyName) {

        run(matchCode, UpdateSBCType.HdcOdds.intValue());
    }

    @Override
    public void updateOdds(Long id) {

        HttpClient httpClient = HttpClientBuilder.create().build();

        for (int i = 0; i < Constants.SPIDER_WEB_HOST_PORTS.length; i++) {
            String uri = Constants.SPIDER_WEB_HOST_PORTS[i] + "/spider-web/syncOdds.do?id=" + id;
            HttpGet get = new HttpGet(uri);
            try {
                httpClient.execute(get);
                infoLogger.info("send to get request uri[" + uri + "]");
            } catch (Throwable e) {
                // 都在一个机器上，几乎不会出错，不做特殊的错误处理
                infoLogger.error("send to get request uri[" + uri + "]", e);
            }
        }
    }

    @Override
    public void updateMatchCode(String matchCode) {

        //rtodo 暂时写死
        update(matchCode, "matlabTopic", matlabTag);
    }

    @Override
    public String getProducerGroup() {

        return producerGroup;
    }

    @Override
    public String getInplayTopic() {

        return inplayTopic;
    }

    @Override
    public String getHdcTag() {

        return hdcTag;
    }

    @Override
    public String getHiloTag() {

        return hiloTag;
    }

    @Override
    public String getScoreAndHalfTag() {

        return scoreAndHalfTag;
    }

    public void run(String matchCode, int type) {

        HttpClient httpClient = HttpClientBuilder.create().build();

        for (int i = 0; i < Constants.SPIDER_WEB_HOST_PORTS.length; i++) {
            String uri = Constants.SPIDER_WEB_HOST_PORTS[i] + "/spider-web/sync.do?matchCode=" + matchCode + "&type=" + type;
            HttpGet get = new HttpGet(uri);
            try {
                httpClient.execute(get);
                infoLogger.info("send to get request uri[" + uri + "]");
            } catch (Throwable e) {
                // 都在一个机器上，几乎不会出错，不做特殊的错误处理
                infoLogger.error("send to get request uri[" + uri + "]", e);
            }
        }
    }
}
