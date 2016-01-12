package com.spider.global;

import com.spider.domain.GamingCompany;
import org.apache.http.HttpHost;

/**
 * 全局的常量
 *
 * @author wsy
 */
public final class Constants {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ crawler ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final String NBSP_HTML = "\u00a0";

    public static final String[] ROCKET_MQ_ADDRS = ConfigProperties.$("rocket.mq.addr").split("\\|");

    public static final String[] ROCKET_MQ_GROUPS = ConfigProperties.$("inplay.odds.group").split("\\|");

    public static final String[] SPIDER_WEB_HOST_PORTS = ConfigProperties.$("inplay.update.web.host").split("\\|");


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ gaming company ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final String LIJI_NAME = GamingCompany.LiJi.getName();

    public static final String JINBAOBO_NAME = GamingCompany.JinBaoBo.getName();


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ http proxy ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final String HOST = ConfigProperties.$("http.proxy.host");

    public static final int PORT = Integer.valueOf(ConfigProperties.$("http.proxy.port"));

    public static final HttpHost AWS_HTTP_PROXY = new HttpHost(HOST, PORT);

    private Constants() {

    }
}
