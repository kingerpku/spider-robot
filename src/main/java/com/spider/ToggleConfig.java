package com.spider;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wsy on 2015/12/22.
 */
public class ToggleConfig {

    public static void main(String[] args) throws IOException {

        File configProperties = new File("E:\\idea-workspace\\robot\\src\\main\\java\\com\\spider\\global\\ConfigProperties.java");
        String lines = FileUtils.readFileToString(configProperties);
        if (lines.contains("//properties.load(new DefaultResourceLoader().getResource(\"file:/opt/spider/config.properties\").getInputStream());")) {
            lines = lines.replace("//properties.load(new DefaultResourceLoader().getResource(\"file:/opt/spider/config.properties\").getInputStream());"
                    , "properties.load(new DefaultResourceLoader().getResource(\"file:/opt/spider/config.properties\").getInputStream());");
            lines = lines.replace("properties.load(new DefaultResourceLoader().getResource(\"classpath:config.properties\").getInputStream());"
                    , "//properties.load(new DefaultResourceLoader().getResource(\"classpath:config.properties\").getInputStream());");
            System.out.println("local to opt");
        } else {
            lines = lines.replace("properties.load(new DefaultResourceLoader().getResource(\"file:/opt/spider/config.properties\").getInputStream());"
                    , "//properties.load(new DefaultResourceLoader().getResource(\"file:/opt/spider/config.properties\").getInputStream());");
            lines = lines.replace("//properties.load(new DefaultResourceLoader().getResource(\"classpath:config.properties\").getInputStream());"
                    , "properties.load(new DefaultResourceLoader().getResource(\"classpath:config.properties\").getInputStream());");
            System.out.println("opt to local");
        }
        FileUtils.writeStringToFile(configProperties, lines);
        System.out.println("toggle ConfigProperties.java success");

        File appConfig = new File("E:\\idea-workspace\\robot\\src\\main\\java\\com\\spider\\config\\AppConfig.java");
        String lines1 = FileUtils.readFileToString(appConfig);
        if (lines1.contains("//@PropertySource(\"file:/opt/spider/config.properties\")")) {
            lines1 = lines1.replace("//@PropertySource(\"file:/opt/spider/config.properties\")"
                    , "@PropertySource(\"file:/opt/spider/config.properties\")");
            lines1 = lines1.replace("@PropertySource(\"classpath:config.properties\")"
                    , "//@PropertySource(\"classpath:config.properties\")");
            System.out.println("local to opt");
        } else {
            lines1 = lines1.replace("@PropertySource(\"file:/opt/spider/config.properties\")"
                    , "//@PropertySource(\"file:/opt/spider/config.properties\")");
            lines1 = lines1.replace("//@PropertySource(\"classpath:config.properties\")"
                    , "@PropertySource(\"classpath:config.properties\")");
            System.out.println("opt to local");
        }
        FileUtils.writeStringToFile(appConfig, lines1);
        System.out.println("toggle AppConfig.java success");

        File config = new File("E:\\idea-workspace\\robot\\src\\main\\resources\\config.properties");
        String lines2 = FileUtils.readFileToString(config);
        if (lines2.contains("500.html.file.path=e:/500") && !lines2.contains("#500.html.file.path=e:/500")) {
            lines2 = lines2.replace("500.html.file.path=e:/500"
                    , "#500.html.file.path=e:/500");
            lines2 = lines2.replace("500.html.file.encoding=gbk"
                    , "500.html.file.encoding=utf8");
            System.out.println("local to opt");
        } else {
            lines2 = lines2.replace("#500.html.file.path=e:/500"
                    , "500.html.file.path=e:/500");
            lines2 = lines2.replace("500.html.file.encoding=utf8"
                    , "500.html.file.encoding=gbk");
            System.out.println("opt to local");
        }
        FileUtils.writeStringToFile(config, lines2);
        System.out.println("toggle config.properties success");

    }
}
