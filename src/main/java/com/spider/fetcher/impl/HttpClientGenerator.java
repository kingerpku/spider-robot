package com.spider.fetcher.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.spider.fetcher.HttpConfig;

import java.util.Map;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class HttpClientGenerator {

    private PoolingHttpClientConnectionManager connectionManager;

    // //http://blog.csdn.net/yanzi1225627/article/details/24937439
    // ThreadSafeClientConnManager替换PoolingHttpClientConnectionManager
    public HttpClientGenerator() {

        SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslSocketFactory).build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setMaxTotal(200);
    }

    public HttpClientGenerator setPoolSize(int poolSize) {

        connectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient getClient(HttpConfig httpConfig, HttpHost proxy) {

        return generateClient(httpConfig, httpConfig.getFollowRedirect(), proxy);
    }

    public CloseableHttpClient generateClient(HttpConfig httpConfig, boolean isRedirect, HttpHost proxy) {

        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
        if (!isRedirect) {
            httpClientBuilder.disableRedirectHandling();
        }
        if (httpConfig != null && StringUtils.isNotEmpty(httpConfig.getHeaders().get("User-Agent"))) {
            httpClientBuilder.setUserAgent(httpConfig.getHeaders().get("User-Agent"));
        } else {
            httpClientBuilder.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
        }
        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        if (httpConfig != null) {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpConfig.getRetryTimes(), true));
        }
        generateCookie(httpClientBuilder, httpConfig);
        if (proxy != null) {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            Credentials credentials = new UsernamePasswordCredentials("caiex", "caiex");//rtodo credentials
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
        }
        return httpClientBuilder.build();
    }

    private void generateCookie(HttpClientBuilder httpClientBuilder, HttpConfig httpConfig) {

        CookieStore cookieStore = new BasicCookieStore();
        if (httpConfig != null && httpConfig.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : httpConfig.getCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(httpConfig.getDomain());
                cookieStore.addCookie(cookie);
            }
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }

}
