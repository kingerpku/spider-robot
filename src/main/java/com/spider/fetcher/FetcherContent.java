package com.spider.fetcher;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FetcherContent {

    private int statusCode;

    private String charset = "UTF-8";

    private byte[] datas;

    private String url;

    private String html;

    private boolean isRedirect;

    private String redirectUrl;

    private String taskName;

    private String filePath;

    private Map<String, Object> beanMap;

    public int getStatusCode() {

        return statusCode;
    }

    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }

    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {

        this.charset = charset;
    }

    public byte[] getDatas() {

        return datas;
    }

    public void setDatas(byte[] datas) {

        this.datas = datas;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getHtml() {

        return html;
    }

    public void setHtml(String html) {

        this.html = html;
    }

    public boolean isRedirect() {

        return isRedirect;
    }

    public void setRedirect(boolean redirect) {

        isRedirect = redirect;
    }

    public String getRedirectUrl() {

        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {

        this.redirectUrl = redirectUrl;
    }

    public String getTaskName() {

        return taskName;
    }

    public void setTaskName(String taskName) {

        this.taskName = taskName;
    }

    public String getFilePath() {

        return filePath;
    }

    public void setFilePath(String filePath) {

        this.filePath = filePath;
    }

    public Map<String, Object> getBeanMap() {

        return beanMap;
    }

    public void setBeanMap(Map<String, Object> beanMap) {

        this.beanMap = beanMap;
    }

}
