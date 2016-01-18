package com.spider.fetcher.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.spider.fetcher.Fetcher;
import com.spider.fetcher.FetcherContent;
import com.spider.fetcher.HttpConfig;
import com.spider.utils.LogHelper;
import com.spider.utils.UrlUtils;

public class HttpClientFetcherImpl implements Fetcher {

    private String className = getClass().getName();

    private static Logger infoLogger = LogHelper.getInfoLogger();

    private static Logger messLogger = LogHelper.getMessLogger();

    private static Logger errorLogger = LogHelper.getErrorLogger();

    // CloseableHttpClient 内置对Cookie和SSL的处理
    private Map<String, CloseableHttpClient> httpClients = new HashMap<>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private Random random = new Random();

    private CloseableHttpClient getHttpClient(HttpConfig httpConfig, HttpHost proxy) {

        if (httpConfig == null) {
            return httpClientGenerator.getClient(null, proxy);
        }
        String domain = httpConfig.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(httpConfig, proxy);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public FetcherContent wgetHtml(String url, HttpConfig httpConfig, HttpHost proxy) {

        LogHelper.infoLog(infoLogger, null, "get url:[{0}] start...", url);
        if (httpConfig == null) {
            httpConfig = new HttpConfig();
        }
        FetcherContent fetcherContent = new FetcherContent();
        fetcherContent.setUrl(url);
        CloseableHttpResponse httpResponse = null;
        try {
            HttpUriRequest httpUriRequest = buildRequest(url, httpConfig);
            httpResponse = getHttpClient(httpConfig, proxy).execute(httpUriRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            fetcherContent.setStatusCode(statusCode);
            LogHelper.infoLog(messLogger, null, "class:{0},method:{1},statusCode:{2},url:{3}", className, "gethtml", statusCode, url);
            if (statusCode == HttpStatus.SC_OK || isSkipStatusCode(httpConfig.getSkipStatusCode(), statusCode + "")) {
                String value = httpResponse.getEntity().getContentType().getValue();
                String charset = UrlUtils.getCharset(value);
                if (StringUtils.isEmpty(charset)) {
                    charset = httpConfig.getCharSet();
                }
                fetcherContent.setHtml(IOUtils.toString(httpResponse.getEntity().getContent(), charset));
            } else if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // get方法http有自动重定向功能post put方法没有
                boolean b;
                if ("GET".equals(httpConfig.getMethod().toUpperCase())) {
                    b = !httpConfig.getFollowRedirect();
                } else {
                    b = httpConfig.getFollowRedirect();
                }
                if (b) {
                    Header[] headers = httpResponse.getHeaders("Location");
                    if (headers != null && headers.length > 0) {
                        fetcherContent.setRedirectUrl(headers[0].getValue());
                        fetcherContent.setRedirect(true);
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(errorLogger, e, "exception class:{0},method:{1},url:{2} request html error", className, "gethtml", url);
            fetcherContent.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (httpResponse != null) {
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (Exception e) {
                LogHelper.errorLog(errorLogger, e, "exception class:{0},method:{1},url:{2} close io error", className, "gethtml", url);
            }
        }
        return fetcherContent;
    }

    public FetcherContent wgetStatusCode(String url, HttpConfig httpConfig, HttpHost proxy) {

        LogHelper.infoLog(infoLogger, null, "class:{0},method:{1},url:{2} start...", className, "wgetStatusCode", url);
        if (httpConfig == null) {
            httpConfig = new HttpConfig();
        }
        FetcherContent fetcherContent = new FetcherContent();
        fetcherContent.setUrl(url);
        CloseableHttpResponse httpResponse = null;
        HttpUriRequest httpUriRequest;
        try {
            httpUriRequest = buildRequest(url, httpConfig);
            httpResponse = getHttpClient(httpConfig, proxy).execute(httpUriRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            fetcherContent.setStatusCode(statusCode);
            LogHelper.infoLog(messLogger, null, "class:{0},method:{1},statusCode:{2},url:{3}", className, "wgetStatusCode", statusCode, url);
            if (statusCode == HttpStatus.SC_OK || isSkipStatusCode(httpConfig.getSkipStatusCode(), statusCode + "")) {

            } else if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // get方法http有自动重定向功能 post put方法没有
                boolean b;
                if ("GET".equals(httpConfig.getMethod().toUpperCase())) {
                    b = !httpConfig.getFollowRedirect();
                } else {
                    b = httpConfig.getFollowRedirect();
                }
                if (b) {
                    Header[] headers;
                    headers = httpResponse.getHeaders("Location");
                    if (headers != null && headers.length > 0) {
                        fetcherContent.setRedirectUrl(headers[0].getValue());
                        fetcherContent.setRedirect(true);
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(errorLogger, e, "exception class:{0},method:{1},url:{2} request html error", className, "wgetStatusCode", url);
            fetcherContent.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (httpResponse != null) {
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (Exception e) {
                LogHelper.errorLog(errorLogger, e, "exception class:{0},method:{1},url:{2} close io error", className, "wgetStatusCode", url);
            }
        }
        return fetcherContent;
    }

    ;

    // 获取文件的扩展名
    public String getFileExtType(HttpResponse response) {

        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String extType = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        String filename = param.getValue();
                        if (StringUtils.isNotEmpty(filename) && filename.contains(".")) {
                            extType = filename.substring(filename.lastIndexOf("."));
                        }
                    } catch (Exception e) {
                        LogHelper.errorLog(errorLogger, e, "get file ext type error", "");
                    }
                }
            }
        }
        return extType;
    }

    private boolean isSkipStatusCode(String skipStatusCode, String currentCode) {

        boolean b = false;
        if (StringUtils.isNotEmpty(skipStatusCode)) {
            for (String str : skipStatusCode.split(",")) {
                if (currentCode.matches(str)) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

    private String escapeNormalizedUrl(String normalizedUrl) {

        try {
            URL url = new URL(normalizedUrl);
            String host = url.getHost();
            String protocol = url.getProtocol();
            String domain = protocol + "://" + host;
            normalizedUrl = normalizedUrl.replace(domain, "");
            normalizedUrl = normalizedUrl.replaceAll("'", "%27");
            normalizedUrl = normalizedUrl.replaceAll("]", "%5D");
            normalizedUrl = normalizedUrl.replaceAll("\\[", "%5B");
            normalizedUrl = normalizedUrl.replaceAll(" ", "%20");
            normalizedUrl = normalizedUrl.replaceAll("\"", "%22");
            normalizedUrl = normalizedUrl.replaceAll("\\|", "%7c");
            normalizedUrl = normalizedUrl.replaceAll("`", "%60");
            normalizedUrl = normalizedUrl.replaceAll("\\{", "%7B");
            normalizedUrl = normalizedUrl.replaceAll("\\}", "%7D");
            normalizedUrl = normalizedUrl.replaceAll("%26apos%3B", "'");
            normalizedUrl = normalizedUrl.replaceAll(" ", "%20");
            // normalizedUrl=normalizedUrl.replaceAll("-","4%2d");
            normalizedUrl = domain + normalizedUrl;
        } catch (Exception e) {
            LogHelper.errorLog(errorLogger, e, "exception  escapeNormalizedUrl url:{0}", normalizedUrl);
        }
        return normalizedUrl;
    }

    private HttpUriRequest buildRequest(String url, HttpConfig httpConfig) {

        RequestBuilder requestBuilder = null;
        if (StringUtils.isEmpty(httpConfig.getMethod()) || "GET".equals(httpConfig.getMethod().toUpperCase())) {
            requestBuilder = RequestBuilder.get().setUri(escapeNormalizedUrl(url));
        } else {
            requestBuilder = RequestBuilder.post().setUri(escapeNormalizedUrl(url));
        }
        for (Map.Entry<String, String> headerEntry : httpConfig.getHeaders().entrySet()) {
            requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        if (httpConfig.getParams() != null) {
            for (Map.Entry<String, String> paramEntry : httpConfig.getParams().entrySet()) {
                requestBuilder.addParameter(paramEntry.getKey(), paramEntry.getValue());
            }
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(httpConfig.getTimeOut())
                .setSocketTimeout(httpConfig.getSocketTimeOut())
                .setConnectTimeout(httpConfig.getTimeOut())
                .setConnectionRequestTimeout(httpConfig.getTimeOut())
                .setCookieSpec(CookieSpecs.BEST_MATCH);
        if (CollectionUtils.isNotEmpty(httpConfig.getProxyHosts())) {
            HttpHost httphost;
            if ((httphost = httpConfig.getProxyHost(random.nextInt(httpConfig.getProxyHosts().size()))) != null) {
                LogHelper.infoLog(infoLogger, null, "use proxy-->ip:{0},port:{1}", httphost.getHostName(), httphost.getPort());
                requestConfigBuilder.setProxy(httphost);
            }
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }

}
