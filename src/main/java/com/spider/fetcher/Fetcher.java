package com.spider.fetcher;

import org.apache.http.HttpHost;

public interface Fetcher {

    FetcherContent wgetHtml(String url, HttpConfig httpConfig, HttpHost proxy);

    FetcherContent wgetStatusCode(String url, HttpConfig httpConfig, HttpHost proxy);
}
