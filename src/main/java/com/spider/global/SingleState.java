package com.spider.global;

/**
 * 彩客网比赛的状态位，表示该场比赛是否开售单关
 *
 * @author wsy
 */
public enum SingleState {

    Yes(0),//有单关
    No(1);//没有单关

    private final int state;

    SingleState(int state) {

        this.state = state;
    }

    public String value() {

        return state + "";
    }

}
