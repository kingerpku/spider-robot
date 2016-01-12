package com.spider.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * url and html utils.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class UrlUtils {

    private static Logger logger = Logger.getLogger(UrlUtils.class);

    /**
     * canonicalizeUrl
     * <p/>
     * Borrowed from Jsoup.
     *
     * @param url
     * @param refer
     * @return canonicalizeUrl
     */
    public static String canonicalizeUrl(String url, String refer) {

        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its
                // own, so try that
                e.printStackTrace();
                LogHelper.errorLog(logger, e, "", "");
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo',
            // not '//path/file?foo' as desired
            if (url.startsWith("?"))
                url = base.getPath() + url;
            URL abs = new URL(base, url);
            return encodeIllegalCharacterInUrl(abs.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogHelper.errorLog(logger, e, "", "");
            return "";
        }
    }

    public static String getCanonicalURL(String href, String context) {

        if (StringUtils.isEmpty(href)) {
            return "";
        }
        if (href.contains("#")) {
            href = href.substring(0, href.indexOf("#"));
        }
        href = href.replace(" ", "%20");
        try {
            // url:
            // download/1165849-a-joystick--ep-02--time-adventure-part-i-mp3.html
            // refer: http://www.musicaddict.com/
            if (StringUtils.isNotEmpty(href)) {
                if (!(href.startsWith("http://") || href.startsWith("https://"))) {
                    if (href.matches("^[a-zA-Z0-9_].*")) {
                        if (StringUtils.isNotEmpty(context)) {
                            context = getHost(context);
                        }
                    }
                }
            }
            URL canonicalURL;
            if (context == null) {
                canonicalURL = new URL(href);
            } else {
                canonicalURL = new URL(new URL(context), href);
            }
            String path = canonicalURL.getPath();
            if (path.startsWith("/../")) {
                path = path.substring(3);
                canonicalURL = new URL(canonicalURL.getProtocol(), canonicalURL.getHost(), canonicalURL.getPort(), path);
            } else if (path.contains("..")) {
                LogHelper.errorLog(logger, null, "getCanonicalURL error href:{0}  context:{1}", href, context);
            }
            return canonicalURL.toExternalForm();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            LogHelper.errorLog(logger, ex, "getCanonicalURL error href:{0}  context:{1}", href, context);
            return null;
        }
    }

    /**
     *
     * @param url
     * @return
     */
    public static String encodeIllegalCharacterInUrl(String url) {

        // TODO more charator support
        return url.replace(" ", "%20");
    }

    public static String getHost(String url) {

        String host = url;
        int i = StringUtils.ordinalIndexOf(url, "/", 3);
        if (i > 0) {
            host = StringUtils.substring(url, 0, i);
        }
        return host;
    }

    private static Pattern patternForProtocal = Pattern.compile("[\\w]+://");

    public static String removeProtocol(String url) {

        return patternForProtocal.matcher(url).replaceAll("");
    }

    public static String getDomain(String url) {

        String domain = removeProtocol(url);
        int i = StringUtils.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        return domain;
    }

    /**
     * allow blank space in quote
     */
    private static Pattern patternForHrefWithQuote = Pattern.compile("(<a[^<>]*href=)[\"']([^\"'<>]*)[\"']", Pattern.CASE_INSENSITIVE);

    /**
     * disallow blank space without quote
     */
    private static Pattern patternForHrefWithoutQuote = Pattern.compile("(<a[^<>]*href=)([^\"'<>\\s]+)", Pattern.CASE_INSENSITIVE);

    public static String fixAllRelativeHrefs(String html, String url) {

        html = replaceByPattern(html, url, patternForHrefWithQuote);
        html = replaceByPattern(html, url, patternForHrefWithoutQuote);
        return html;
    }

    public static String replaceByPattern(String html, String url, Pattern pattern) {

        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = pattern.matcher(html);
        int lastEnd = 0;
        boolean modified = false;
        while (matcher.find()) {
            modified = true;
            stringBuilder.append(StringUtils.substring(html, lastEnd, matcher.start()));
            stringBuilder.append(matcher.group(1));
            stringBuilder.append("\"").append(canonicalizeUrl(matcher.group(2), url)).append("\"");
            lastEnd = matcher.end();
        }
        if (!modified) {
            return html;
        }
        stringBuilder.append(StringUtils.substring(html, lastEnd));
        return stringBuilder.toString();
    }

    private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");

    public static String getCharset(String contentType) {

        try {
            Matcher matcher = patternForCharset.matcher(contentType);
            if (matcher.find()) {
                String charset = matcher.group(1);
                if (Charset.isSupported(charset)) {
                    return charset;
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(logger, e, "", "");
        }
        return null;
    }

    public static boolean checkUrlHost(String url, List<String> hosts, String referer) {

        try {
            if (CollectionUtils.isNotEmpty(hosts)) {
                if (StringUtils.isNotEmpty(url)) {
                    if (!url.startsWith("http")) {
                        url = UrlUtils.getCanonicalURL(url, referer);
                    }
                    String url_host = new URL(url).getHost();
                    for (String host : hosts) {
                        // 可以改成equal 匹配 这里先用正则匹配
                        if (url_host.matches(host)) {
                            return true;
                        }
                    }
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.errorLog(logger, e, "", "");
        }
        return false;
    }

    public static void main(String[] args) {

        String url = "library/mp3/page-2";
        String refer = "http://www.musicaddict.com/library/";
        // String str=canonicalizeUrl(url,refer);
        // System.out.println(str);
        String str = getCanonicalURL(url, refer);

        System.out.println(str);

    }
}
