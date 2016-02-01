package com.spider.domain.pinnacle;

/**
 * Created by wsy on 2016/1/28.
 */
public class InrunningEvents {

    Long id;

    Integer state;

    Integer elapsed;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Integer getState() {

        return state;
    }

    public void setState(Integer state) {

        this.state = state;
    }

    public Integer getElapsed() {

        return elapsed;
    }

    public void setElapsed(Integer elapsed) {

        this.elapsed = elapsed;
    }

    @Override
    public String toString() {

        return "InrunningEvents{" +
                "id=" + id +
                ", state=" + state +
                ", elapsed=" + elapsed +
                '}';
    }
}
