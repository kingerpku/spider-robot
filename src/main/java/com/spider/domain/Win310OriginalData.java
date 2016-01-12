package com.spider.domain;

import com.google.common.collect.Sets;
import com.spider.entity.TCrawlerWin310;

import java.util.Set;

/**
 * 这个类用于存放解析一个{@link TCrawlerWin310}实体需要的所有信息
 *
 * @author wsy
 */
public class Win310OriginalData implements OriginalData {

    private static final long serialVersionUID = 1L;

    /**
     * 利记公司的赔率页面链接
     */
    private String lijiLink;

    /**
     * 利记公司的赔率页面
     */
    private String lijiContent;

    /**
     * 金宝博公司的赔率页面链接
     */
    private String jinbaoboLink;

    /**
     * 金宝博公司的赔率页面
     */
    private String jinbaoboContent;

    /**
     * 主页面链接
     */
    private String mainLink;

    /**
     * 主页面内容，内容为每场比赛所在行拼出的html内容，用于解析，格式如下
     * <p/>
     * <html><body>
     * <table id='xxx'>
     * <tbody> + tr + </tbody>
     * </table>
     * </body></html>
     */
    private String mainContent;

    private String win310EuropeId;

    private Set<String> singleMatchSet = Sets.newHashSet();

    public String getLijiLink() {

        return lijiLink;
    }

    public void setLijiLink(String lijiLink) {

        this.lijiLink = lijiLink;
    }

    public String getLijiContent() {

        return lijiContent;
    }

    public void setLijiContent(String lijiContent) {

        this.lijiContent = lijiContent;
    }

    public String getJinbaoboLink() {

        return jinbaoboLink;
    }

    public void setJinbaoboLink(String jinbaoboLink) {

        this.jinbaoboLink = jinbaoboLink;
    }

    public String getJinbaoboContent() {

        return jinbaoboContent;
    }

    public void setJinbaoboContent(String jinbaoboContent) {

        this.jinbaoboContent = jinbaoboContent;
    }

    public String getMainLink() {

        return mainLink;
    }

    public void setMainLink(String mainLink) {

        this.mainLink = mainLink;
    }

    public String getMainContent() {

        return mainContent;
    }

    public void setMainContent(String mainContent) {

        this.mainContent = mainContent;
    }

    public static long getSerialversionuid() {

        return serialVersionUID;
    }

    public String getWin310EuropeId() {

        return win310EuropeId;
    }

    public void setWin310EuropeId(String win310EuropeId) {

        this.win310EuropeId = win310EuropeId;
    }

    @Override
    public String toString() {

        return "Win310OriginalData [lijiLink=" + lijiLink + ", jinbaoboLink=" + jinbaoboLink + ", mainLink=" + mainLink + ", win310EuropeId=" + win310EuropeId + "]";
    }

    public Set<String> getSingleMatchSet() {

        return singleMatchSet;
    }

    public void setSingleMatchSet(Set<String> singleMatchSet) {

        this.singleMatchSet = singleMatchSet;
    }
}
