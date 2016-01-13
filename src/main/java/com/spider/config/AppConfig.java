package com.spider.config;

import com.gargoylesoftware.htmlunit.WebClient;
import com.spider.global.Constants;
import com.spider.robot.CompanyOddsRobot;
import com.spider.robot.PinnaclesRobot;
import com.spider.robot.ThreeInOneParser;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import sun.misc.BASE64Encoder;

import java.nio.charset.Charset;

/**
 * Spring的配置类，负责应用的整体配置
 *
 * @author wsy
 */
@Configuration
@Import(value = {
        DataBaseConfig.class
})
//@PropertySource("file:/opt/spider/config.properties")
@PropertySource("classpath:config.properties")
@ComponentScan(basePackages = {"com.spider"})
public class AppConfig {


    public static final String USER_PWD = "WF425817:fw19861210";

    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setLocation(new ClassPathResource("config.properties"));
        return propertySourcesPlaceholderConfigurer;
    }

    /**
     * @return 返回利记的解析器
     */
    @Bean
    public ThreeInOneParser ljParser() {

        return new ThreeInOneParser(Constants.LIJI_NAME);
    }

    /**
     * @return 返回金宝博的解析
     */
    @Bean
    public ThreeInOneParser jbbParser() {

        return new ThreeInOneParser(Constants.JINBAOBO_NAME);
    }

    /**
     * @return 返回利记的解析器
     */
    @Bean
    public CompanyOddsRobot ljRobot() {

        return new CompanyOddsRobot(Constants.LIJI_NAME);
    }

    /**
     * @return 返回金宝博的解析
     */
    @Bean
    public CompanyOddsRobot jbbRobot() {

        return new CompanyOddsRobot(Constants.JINBAOBO_NAME);
    }

    @Bean
    public WebClient webClient() {

        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.addRequestHeader("Authorization", "Basic " + new BASE64Encoder().encode(USER_PWD.getBytes(Charset.forName("utf-8"))));
        return webClient;
    }

}
