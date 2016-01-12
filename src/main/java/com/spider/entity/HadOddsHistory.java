package com.spider.entity;

// Generated 2015-7-21 10:41:12 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;

/**
 * HhadOdds generated by hbm2java
 */
@Entity
@Table(name = "had_odds_history")
public class HadOddsHistory implements java.io.Serializable {

    private static final long serialVersionUID = -5234128970474345447L;

    private Long id;

    private String gamingCompany = "";

    private String oddsA = "";

    private String oddsD = "";

    private String oddsH = "";

    private Date updateTime = new Date();

    private String oddsUpdateTime = "";

    private String state = "";

    private String score = "";

    private Long win310Id;

    private String durationTime = "";
    private Integer homeRedCard = 0;

    private Integer awayRedCard = 0;

    private Integer europeId;

    @Column(name = "EUROPE_ID")
    public Integer getEuropeId() {

        return europeId;
    }

    public void setEuropeId(Integer europeId) {

        this.europeId = europeId;
    }

    @Column(name = "HOME_RED_CARD")
    public Integer getHomeRedCard() {

        return homeRedCard;
    }

    public void setHomeRedCard(Integer homeRedCard) {

        this.homeRedCard = homeRedCard;
    }

    @Column(name = "AWAY_RED_CARD")
    public Integer getAwayRedCard() {

        return awayRedCard;
    }

    public void setAwayRedCard(Integer awayRedCard) {

        this.awayRedCard = awayRedCard;
    }
    public void setDurationTime(String durationTime) {

        if (StringUtils.isBlank(durationTime)) {
            this.durationTime = "";
            return;
        }
        this.durationTime = durationTime;

    }

    @Column(name = "DURATION_TIME",  length = 5)
    public String getDurationTime() {

        return durationTime;
    }

    public HadOddsHistory() {

    }

    public HadOddsHistory(HadOdds hadOdds) {

        this.gamingCompany = hadOdds.getGamingCompany();
        this.oddsA = hadOdds.getOddsA();
        this.oddsD = hadOdds.getOddsD();
        this.oddsH = hadOdds.getOddsH();
        this.updateTime = hadOdds.getUpdateTime();
        this.oddsUpdateTime = hadOdds.getOddsUpdateTime();
        this.state = hadOdds.getState();
        this.score = hadOdds.getScore();
        this.win310Id = hadOdds.getWin310Id();
        this.durationTime = hadOdds.getDurationTime();
        this.homeRedCard = hadOdds.getHomeRedCard();
        this.awayRedCard = hadOdds.getAwayRedCard();
        this.europeId = hadOdds.getEuropeId();
    }

    @Column(name = "WIN310_ID", nullable = false)
    public Long getWin310Id() {

        return win310Id;
    }

    public void setWin310Id(Long win310Id) {

        this.win310Id = win310Id;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @Column(name = "GAMING_COMPANY", nullable = false, length = 50)
    public String getGamingCompany() {

        return this.gamingCompany;
    }

    public void setGamingCompany(String gamingCompany) {

        this.gamingCompany = gamingCompany;
    }

    @Column(name = "ODDS_A", nullable = false, length = 5)
    public String getOddsA() {

        return this.oddsA;
    }

    public void setOddsA(String oddsA) {

        this.oddsA = oddsA;
    }

    @Column(name = "ODDS_D", nullable = false, length = 10)
    public String getOddsD() {

        return this.oddsD;
    }

    public void setOddsD(String oddsD) {

        this.oddsD = oddsD;
    }

    @Column(name = "ODDS_H", nullable = false, length = 5)
    public String getOddsH() {

        return this.oddsH;
    }

    public void setOddsH(String oddsH) {

        this.oddsH = oddsH;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME", length = 19)
    public Date getUpdateTime() {

        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {

        this.updateTime = updateTime;
    }

    @Column(name = "ODDS_UPDATE_TIME", nullable = false, length = 20)
    public String getOddsUpdateTime() {

        return this.oddsUpdateTime;
    }

    public void setOddsUpdateTime(String oddsUpdateTime) {

        this.oddsUpdateTime = oddsUpdateTime;
    }

    @Column(name = "STATE", nullable = false, length = 2)
    public String getState() {

        return state;
    }

    public void setState(String state) {

        this.state = state;
    }

    @Column(name = "SCORE", nullable = false, length = 2)
    public String getScore() {

        return score;
    }

    public void setScore(String score) {

        this.score = score;
    }

    @Override
    public String toString() {

        return "HadOddsHistory{" +
                "id=" + id +
                ", gamingCompany='" + gamingCompany + '\'' +
                ", oddsA='" + oddsA + '\'' +
                ", oddsD='" + oddsD + '\'' +
                ", oddsH='" + oddsH + '\'' +
                ", updateTime=" + updateTime +
                ", oddsUpdateTime='" + oddsUpdateTime + '\'' +
                ", state='" + state + '\'' +
                ", score='" + score + '\'' +
                ", win310Id=" + win310Id +
                ", durationTime='" + durationTime + '\'' +
                ", homeRedCard=" + homeRedCard +
                ", awayRedCard=" + awayRedCard +
                ", europeId=" + europeId +
                '}';
    }
}
