package com.spider.utils;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wsy on 2015/12/22.
 *
 * @author wsy
 */
public class RobotUtils {

    /**
     * ��ȡΨһid��yyyyMMdd+matchCode
     *
     * @param startDateTime
     * @param matchCode
     * @return
     */
    public static Long getUniqueMatchId(Date startDateTime, String matchCode) {

        return Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(startDateTime) + matchCode);
    }

    /**
     * ���ݴ���html�ļ�����HtmlPage�������ڽ���
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static HtmlPage getHtmlPageFromFile(File file, String charset) throws IOException {

        WebClient client = new WebClient();
        try {
            String content = FileUtils.readFileToString(file);
            URL url = new URL("http://www.example.com");
            StringWebResponse response = new StringWebResponse(content, charset, url);
            client.getOptions().setJavaScriptEnabled(false);
            return HTMLParser.parseHtml(response, client.getCurrentWindow());
        } finally {
            client.closeAllWindows();
        }
    }
}
