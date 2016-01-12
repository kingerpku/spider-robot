package com.spider.domain;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by wsy on 2016/1/11.
 */
public class CompanyOddsParam {

    private HtmlPage htmlPage;

    private String companyName;

    private Integer europeId;

    public HtmlPage getHtmlPage() {

        return htmlPage;
    }

    public void setHtmlPage(HtmlPage htmlPage) {

        this.htmlPage = htmlPage;
    }

    public String getCompanyName() {

        return companyName;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public Integer getEuropeId() {

        return europeId;
    }

    public void setEuropeId(Integer europeId) {

        this.europeId = europeId;
    }
}
