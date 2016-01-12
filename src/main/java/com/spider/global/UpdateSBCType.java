package com.spider.global;

/**
 * 向sbc推送的数据类型
 *
 * @author wsy
 */
public enum UpdateSBCType {

    HiloOdds(1),//大小球的赔率
    HdcOdds(2),//hdc的赔率
    ScoreAndHalf(3);//比分和上下半场的变化情况

    private int type;

    UpdateSBCType(int type) {

        this.type = type;
    }

    public int intValue() {

        return this.type;
    }
}
