package com.spider.sbc;

import com.spider.entity.HdcOdds;
import com.spider.entity.HiloOdds;
import com.spider.entity.TCrawlerWin310;

/**
 * 与sbc通信的接口，当爬虫赔率有更新时，需要向sbc推送相关信息，通过此接口完成
 *
 * @author wsy
 */
public interface SbcUpdateManager {

    <T> void update(T data, String tag);

    <T> void update(T data, String topic, String tag);

    /**
     * 向sbc推送半场情况和比分情况，内部通过调用spider-web的web接口实现
     *
     * @param win310
     * @param matchCode
     */
    void updateSbcScoreAndHalf(TCrawlerWin310 win310, String matchCode);

    /**
     * 向sbc推送hilo赔率变化情况，内部通过调用spider-web的web接口实现
     *
     * @param hiloOdds
     * @param matchCode
     * @param companyName
     */
    void updateSbcHiloOdds(HiloOdds hiloOdds, String matchCode, String companyName);

    /**
     * 向sbc推送hdc赔率变化情况，内部通过调用spider-web的web接口实现
     *
     * @param hdcOdds
     * @param matchCode
     * @param companyName
     */
    void updateSbcHdcOdds(HdcOdds hdcOdds, String matchCode, String companyName);

    /**
     * 向队列发送matchCode，用于通知matlab_service进行计算
     *
     * @param matchCode
     */
    void updateMatchCode(String matchCode);

    String getProducerGroup();

    String getInplayTopic();

    String getHdcTag();

    String getHiloTag();

    String getScoreAndHalfTag();
}
