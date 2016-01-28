package com.spider.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.spider.dao.PinnaclesDao;
import com.spider.domain.pinnacle.InrunningEvents;
import com.spider.entity.PinnacleEntity;
import com.spider.utils.LogHelper;
import com.spider.utils.XmlUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通过pinnacle开放api获取数据
 *
 * @author wsy
 */
@Component
public class PinnaclesRobot implements Runnable {

    @Autowired
    private WebClient webClient;

    @Autowired
    private PinnaclesDao pinnaclesDao;

    private Logger logger = Logger.getLogger("pinnacle_logger");

    private long lastOddsUpdateTime;

    public List<PinnacleEntity> getPinnacles() throws IOException, DocumentException, ParseException {

        List<PinnacleEntity> entities = new ArrayList<>();
        try {
            XmlPage xmlPage = webClient.getPage("https://api.pinnaclesports.com/v1/feed?sportid=29&oddsformat=1&last=" + lastOddsUpdateTime);
            String xml = xmlPage.getContent();
            List<Node> nodes = XmlUtils.selectNodes(xml, "//league");
            lastOddsUpdateTime = Long.parseLong(XmlUtils.selectNode(xml, "//fdTime").getText());
            int count = 0;
            for (Node node : nodes) {
                List<Node> eventNodes = node.selectNodes("events/event");
                for (Node eventNode : eventNodes) {
                    Date startDateTime = DateUtils.parseDate(eventNode.valueOf("startDateTime").replace('T', ' ').replaceAll("Z", ""), "yyyy-MM-dd HH:mm:ss");
                    if (startDateTime.after(DateUtils.addDays(new Date(), 2))) {
                        System.out.println(++count);
                        continue;
                    }
                    PinnacleEntity event = parseEvent(eventNode, Integer.valueOf(node.valueOf("id")), startDateTime);
                    entities.add(event);
                }
            }
        } catch (Exception e) {
            LogHelper.error(logger, "Exception", e);
        } finally {
            webClient.closeAllWindows();
        }
        return entities;
    }

    private PinnacleEntity parseEvent(Node eventNode, Integer leagueId, Date startDateTime) throws ParseException {

        PinnacleEntity pinnacleEntity = new PinnacleEntity();
        Long id = Long.valueOf(eventNode.valueOf("id"));
        pinnacleEntity.setEventId(id);
        LogHelper.info(logger, "start parse pinnacle entity [" + id + "]");
        pinnacleEntity.setHomeTeam(eventNode.valueOf("homeTeam/name/text()"));
        pinnacleEntity.setHomePrice(new BigDecimal(eventNode.valueOf("periods/period/spreads/spread/homePrice/text()")));
        pinnacleEntity.setHomeSpread(Double.valueOf(eventNode.valueOf("periods/period/spreads/spread/homeSpread/text()")));
        pinnacleEntity.setAwayTeam(eventNode.valueOf("awayTeam/name/text()"));
        pinnacleEntity.setAwayPrice(new BigDecimal(eventNode.valueOf("periods/period/spreads/spread/awayPrice/text()")));
        pinnacleEntity.setAwaySpread(Double.valueOf(eventNode.valueOf("periods/period/spreads/spread/awaySpread/text()")));
        pinnacleEntity.setCutOffDateTime(new Timestamp(DateUtils.parseDate(eventNode.valueOf("//periods/period/cutoffDateTime").replace('T', ' ').replaceAll("Z", ""), "yyyy-MM-dd HH:mm:ss").getTime()));
        pinnacleEntity.setStartDateTime(new Timestamp(startDateTime.getTime()));
        pinnacleEntity.setLeagueId(leagueId);
        pinnacleEntity.setIsLive(eventNode.valueOf("IsLive").equals("No") ? false : true);
        pinnacleEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        LogHelper.info(logger, "end parse pinnacle entity " + pinnacleEntity);
        return pinnacleEntity;
    }

    public List<InrunningEvents> getInrunnings() throws IOException, DocumentException {

        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.addRequestHeader("Authorization", "Basic " + new BASE64Encoder().encode("WF425817:fw19861210".getBytes(Charset.forName("utf-8"))));

        List<InrunningEvents> inrunningEvents = new ArrayList<>();
        try {
            UnexpectedPage xmlPage = webClient.getPage("https://api.pinnaclesports.com/v1/inrunning");
            JSONObject inrunning = JSON.parseObject(xmlPage.getWebResponse().getContentAsString());

            JSONArray leagues = inrunning.getJSONArray("sports").getJSONObject(0).getJSONArray("leagues");
            for (int i = 0; i < leagues.size(); i++) {
                JSONArray events = leagues.getJSONObject(i).getJSONArray("events");
                for (int j = 0; j < events.size(); j++) {
                    InrunningEvents inrunningEvent = JSON.parseObject(events.getJSONObject(j).toJSONString(), InrunningEvents.class);
                    inrunningEvents.add(inrunningEvent);
                }
            }
            /**
             * Value	Description
             1	        First half in progress
             2	        Half time in progress
             3	        Second half in progress
             4	        End of regular time
             5	        First half extra time in progress
             6	        Extra time half time in progress
             7	        Second half extra time in progress
             8	        End of extra time
             9	        End of Game
             10	        Game is temporary suspended
             11	        Penalties in progress
             */
        } finally {
            webClient.closeAllWindows();
        }
        return inrunningEvents;
    }

    @Override
    public void run() {

        List<PinnacleEntity> list = null;
        try {
            list = getPinnacles();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.error(logger, "Exception", e);
        }
        for (PinnacleEntity pinnacleEntity : list) {
            pinnaclesDao.saveOrUpdate(pinnacleEntity);
        }
    }

}
