package com.spider.robot;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by wsy on 2015/11/30.
 */
public class Win310RobotTest {

    /**
     * 彩客网竞彩足球过关在售比赛页面
     */
    private String pageUrl = "http://www.310win.com/buy/JingCai2.aspx?TypeID=105&OddsType=2";

    private static WebClient webClient;

    @BeforeClass
    public static void before() {

        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }

    @Test
    public void testMatchListPage() throws IOException {

        final HtmlPage page = webClient.getPage(pageUrl);
        DomElement domElement = page.getElementById("new_buy_main");
        Assert.assertTrue(domElement != null);
    }

    @AfterClass
    public static void afterClass() {

        webClient.closeAllWindows();
    }

}
