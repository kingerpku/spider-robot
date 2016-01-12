package com.spider.robot;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.spider.dao.PinnaclesDao;
import com.spider.domain.pinnacle.PinnacleLeague;
import com.spider.domain.pinnacle.Sport;
import com.spider.entity.PinnacleEntity;
import com.spider.repository.PinnacleRepository;
import com.spider.utils.XmlUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
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

    private long lastOddsUpdateTime;

    public List<Sport> getSports() throws IOException {

        XmlPage xmlPage = webClient.getPage("https://api.pinnaclesports.com/v1/sports");
        List<Node> sportNodes = null;
        List<Sport> sports = new ArrayList<>();
        try {
            sportNodes = XmlUtils.selectNodes(xmlPage.getContent(), "//sport");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        for (Node node : sportNodes) {
            Sport sport = new Sport();
            sport.setName(node.getText());
            sport.setSportId(Integer.valueOf(node.valueOf("@id")));
            sports.add(sport);
        }
        return sports;
    }

    public List<PinnacleEntity> getPinnacles() throws IOException, DocumentException, ParseException {

        XmlPage xmlPage = webClient.getPage("https://api.pinnaclesports.com/v1/feed?sportid=29&oddsformat=1&last=" + lastOddsUpdateTime);
        String xml = xmlPage.getContent();
        List<Node> nodes = XmlUtils.selectNodes(xml, "//league");
        List<PinnacleEntity> entities = new ArrayList<>();
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
        return entities;
    }

    private PinnacleEntity parseEvent(Node eventNode, Integer leagueId, Date startDateTime) throws ParseException {

        PinnacleEntity pinnacleEntity = new PinnacleEntity();
        pinnacleEntity.setHomeTeam(eventNode.valueOf("homeTeam/name/text()"));
        pinnacleEntity.setHomePrice(new BigDecimal(eventNode.valueOf("periods/period/spreads/spread/homePrice/text()")));
        pinnacleEntity.setHomeSpread(Double.valueOf(eventNode.valueOf("periods/period/spreads/spread/homeSpread/text()")));
        pinnacleEntity.setAwayTeam(eventNode.valueOf("awayTeam/name/text()"));
        pinnacleEntity.setAwayPrice(new BigDecimal(eventNode.valueOf("periods/period/spreads/spread/awayPrice/text()")));
        pinnacleEntity.setAwaySpread(Double.valueOf(eventNode.valueOf("periods/period/spreads/spread/awaySpread/text()")));
        pinnacleEntity.setCutOffDateTime(new Timestamp(DateUtils.parseDate(eventNode.valueOf("//periods/period/cutoffDateTime").replace('T', ' ').replaceAll("Z", ""), "yyyy-MM-dd HH:mm:ss").getTime()));
        pinnacleEntity.setStartDateTime(new Timestamp(startDateTime.getTime()));
        pinnacleEntity.setEventId(Long.valueOf(eventNode.valueOf("id")));
        pinnacleEntity.setLeagueId(leagueId);
        pinnacleEntity.setIsLive(eventNode.valueOf("IsLive").equals("No") ? false : true);
        pinnacleEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        return pinnacleEntity;
    }

    public List<PinnacleLeague> getLeagues() throws IOException, DocumentException {

        XmlPage xmlPage = webClient.getPage("https://api.pinnaclesports.com/v1/leagues?sportid=29");
        List<PinnacleLeague> list = new ArrayList<>();
        List<Node> nodes = XmlUtils.selectNodes(xmlPage.getContent(), "//league");
        for (Node node : nodes) {
            PinnacleLeague league = new PinnacleLeague();
            league.setLeagueId(Integer.valueOf(node.valueOf("@id")));
            league.setName(node.getText());
            list.add(league);
        }
        return list;
    }

    @Override
    public void run() {

        List<PinnacleEntity> list = null;
        try {
            list = getPinnacles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (PinnacleEntity pinnacleEntity : list) {
            pinnaclesDao.saveOrUpdate(pinnacleEntity);
        }
    }
}
