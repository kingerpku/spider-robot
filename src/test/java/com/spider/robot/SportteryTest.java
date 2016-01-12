package com.spider.robot;

import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by wsy on 2015/11/30.
 */
public class SportteryTest {

    private String pageUrl = "http://info.sporttery.cn/interface/interface_mixed.php?action=fb_list&pke={0}&_={1}";

    private String sportterUrl = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&poolcode[]=hhad&poolcode[]=had";

    private static WebClient webClient;

    @Before
    public void setUp() throws Exception {

        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        pageUrl = MessageFormat.format(pageUrl, new Random().nextInt(1), new Date().getTime());
    }

    @Test
    public void testSportteryAllJson() throws IOException {

        JavaScriptPage javaScriptPage = webClient.getPage(pageUrl);
        assertTrue(javaScriptPage.getContent().contains("var data="));
    }

    @Test
    public void testSportteryJson() throws IOException {

        TextPage textPage = webClient.getPage(sportterUrl);
        String content = textPage.getContent();
        assertTrue(content.contains("last_updated") && content.contains("data"));
    }

    @After
    public void tearDown() throws Exception {

        webClient.closeAllWindows();
    }
}