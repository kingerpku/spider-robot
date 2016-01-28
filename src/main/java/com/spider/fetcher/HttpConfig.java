package com.spider.fetcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpHost;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpConfig {

    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<>();

    static {
        DEFAULT_HEADERS.put("Accept-Encoding", "gzip,deflate,sdch");
        DEFAULT_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
        DEFAULT_HEADERS.put("Connection", "keep-alive");
        try {
            DEFAULT_HEADERS.put("Authorization", "Basic " + new BASE64Encoder().encode("caiex:caiex".getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            //ignored
        }
    }

    private static final Map<String, String> DEFAULT_COOKIES = new HashMap<>();

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final int DEFAULT_RETRYTIME = 3;

    public static final int DEFAULT_TIMEOUT = 40000;

    public static final int DEFAULT_SOCKET_TIMEOUT = DEFAULT_TIMEOUT * 10;

    public static final boolean DEFAULT_IS_FOLLOWREDIRECT = true;

    private Map<String, String> headers;

    private Map<String, String> cookies;

    private Map<String, String> params = new HashMap<String, String>();

    private int timeOut;

    private int socketTimeOut;

    private String charSet;

    private Boolean isFollowRedirect;

    private int retryTimes;

    private String url;

    private String domain;

    private List<HttpHost> proxyHosts;

    private String method = "GET";

    private String skipStatusCode = "";

    public HttpConfig() {

        this.headers = DEFAULT_HEADERS;
        this.cookies = DEFAULT_COOKIES;
        this.charSet = DEFAULT_CHARSET;
        this.retryTimes = DEFAULT_RETRYTIME;
        this.timeOut = DEFAULT_TIMEOUT;
        this.isFollowRedirect = DEFAULT_IS_FOLLOWREDIRECT;
        this.socketTimeOut = DEFAULT_SOCKET_TIMEOUT;
    }

    public String getMethod() {

        return method;
    }

    public HttpConfig setMethod(String method) {

        this.method = method;
        return this;
    }

    public Map<String, String> getHeaders() {

        return headers;
    }

    public HttpConfig setHeaders(Map<String, String> headers) {

        this.headers = headers;
        return this;
    }

    public void addHeader(String key, String value) {

        headers.put(key, value);
    }

    public Map<String, String> getCookies() {

        return cookies;
    }

    public HttpConfig setCookie(Map<String, String> cookies) {

        this.cookies = cookies;
        return this;
    }

    public void addCookie(String key, String value) {

        cookies.put(key, value);
    }

    public String getCharSet() {

        return charSet;
    }

    public HttpConfig setCharSet(String charSet) {

        this.charSet = charSet;
        return this;
    }

    public Boolean getFollowRedirect() {

        return isFollowRedirect;
    }

    public HttpConfig setFollowRedirect(Boolean followRedirect) {

        isFollowRedirect = followRedirect;
        return this;
    }

    public int getRetryTimes() {

        return retryTimes;
    }

    public HttpConfig setRetryTimes(int retryTimes) {

        this.retryTimes = retryTimes;
        return this;
    }

    public Map<String, String> getParams() {

        return params;
    }

    public HttpConfig setParams(Map<String, String> param) {

        this.params = param;
        return this;
    }

    public HttpConfig addParam(String key, String value) {

        this.params.put(key, value);
        return this;
    }

    public String getDomain() {

        return domain;
    }

    public HttpConfig setDomain(String domain) {

        this.domain = domain;
        return this;
    }

    public int getSocketTimeOut() {

        return socketTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {

        this.socketTimeOut = socketTimeOut;
    }

    public int getTimeOut() {

        return timeOut;
    }

    public HttpConfig setTimeOut(int timeOut) {

        this.timeOut = timeOut;
        return this;
    }

    public List<HttpHost> getProxyHosts() {

        return proxyHosts;
    }

    public HttpHost getProxyHost(int i) {

        if (CollectionUtils.isNotEmpty(proxyHosts)) {
            if (proxyHosts.size() >= i && i > -1) {
                return proxyHosts.get(i);
            } else {
                return proxyHosts.get(0);
            }
        } else {
            return null;
        }
    }

    public HttpConfig setProxyHost(List<HttpHost> proxyHosts) {

        this.proxyHosts = proxyHosts;
        return this;
    }

    public HttpConfig addProxyHost(HttpHost proxyHost) {

        if (CollectionUtils.isEmpty(proxyHosts)) {
            proxyHosts = new ArrayList<HttpHost>();
        }
        proxyHosts.add(proxyHost);
        return this;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getSkipStatusCode() {

        return skipStatusCode;
    }

    public void setSkipStatusCode(String skipStatusCode) {

        this.skipStatusCode = skipStatusCode;
    }

}
