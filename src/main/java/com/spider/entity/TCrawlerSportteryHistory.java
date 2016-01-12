package com.spider.entity;

// Generated 2015-7-14 17:26:36 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TCrawlerSporttery generated by hbm2java
 */
@Entity
@Table(name = "t_crawler_sporttery_history")
public class TCrawlerSportteryHistory implements java.io.Serializable {

    private static final long serialVersionUID = 3617494759613633193L;

    private Long id;

    private String competitionNum;

    private Date startDate;

    private String BDate;

    private Date lastUpdated;

    private String status;

    private String matchs;

    private String homeTeam;

    private String visitionTeam;

    private Integer winCountOne;

    private String statusOne;

    private Integer winCountTwo;

    private String statusTwo;

    private String winProbabilityOne;

    private String failProbabilityOne;

    private String drawProbabilityOne;

    private String winProbabilityTwo;

    private String failProbabilityTwo;

    private String drawProbabilityTwo;

    private Date updateTime = new Date();
    private Date stopSaleTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STOP_SALE_TIME", length = 19)
    public Date getStopSaleTime() {

        return stopSaleTime;
    }

    public void setStopSaleTime(Date stopSaleTime) {

        this.stopSaleTime = stopSaleTime;
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

    @Column(name = "COMPETITION_NUM", nullable = false, length = 10)
    public String getCompetitionNum() {

        return this.competitionNum;
    }

    public void setCompetitionNum(String competitionNum) {

        this.competitionNum = competitionNum.trim();
    }

    @Column(name = "START_DATE_TIME", nullable = false, length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartDate() {

        return this.startDate;
    }

    public void setStartDate(Date startDate) {

        this.startDate = startDate;
    }

    @Column(name = "B_DATE", nullable = false, length = 10)
    public String getBDate() {

        return this.BDate;
    }

    public void setBDate(String BDate) {

        this.BDate = BDate.trim();
    }

    @Column(name = "LAST_UPDATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastUpdated() {

        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {

        this.lastUpdated = lastUpdated;
    }

    @Column(name = "STATUS", nullable = false, length = 20)
    public String getStatus() {

        return this.status;
    }

    public void setStatus(String status) {

        this.status = status.trim();
    }

    @Column(name = "MATCHS", nullable = false, length = 20)
    public String getMatchs() {

        return this.matchs;
    }

    public void setMatchs(String matchs) {

        this.matchs = matchs.trim();
    }

    @Column(name = "HOME_TEAM", nullable = false, length = 50)
    public String getHomeTeam() {

        return this.homeTeam;
    }

    public void setHomeTeam(String homeTeam) {

        this.homeTeam = homeTeam.trim();
    }

    @Column(name = "VISITION_TEAM", nullable = false, length = 50)
    public String getVisitionTeam() {

        return this.visitionTeam;
    }

    public void setVisitionTeam(String visitionTeam) {

        this.visitionTeam = visitionTeam.trim();
    }

    @Column(name = "WIN_COUNT_ONE")
    public Integer getWinCountOne() {

        return this.winCountOne;
    }

    public void setWinCountOne(Integer winCountOne) {

        this.winCountOne = winCountOne;
    }

    @Column(name = "STATUS_ONE", length = 20)
    public String getStatusOne() {

        return this.statusOne;
    }

    public void setStatusOne(String statusOne) {

        this.statusOne = statusOne.trim();
    }

    @Column(name = "WIN_COUNT_TWO")
    public Integer getWinCountTwo() {

        return this.winCountTwo;
    }

    public void setWinCountTwo(Integer winCountTwo) {

        this.winCountTwo = winCountTwo;
    }

    @Column(name = "STATUS_TWO", length = 20)
    public String getStatusTwo() {

        return this.statusTwo;
    }

    public void setStatusTwo(String statusTwo) {

        this.statusTwo = statusTwo.trim();
    }

    @Column(name = "WIN_PROBABILITY_ONE", length = 5)
    public String getWinProbabilityOne() {

        return this.winProbabilityOne;
    }

    public void setWinProbabilityOne(String winProbabilityOne) {

        this.winProbabilityOne = winProbabilityOne;
    }

    @Column(name = "FAIL_PROBABILITY_ONE", length = 5)
    public String getFailProbabilityOne() {

        return this.failProbabilityOne;
    }

    public void setFailProbabilityOne(String failProbabilityOne) {

        this.failProbabilityOne = failProbabilityOne.trim();
    }

    @Column(name = "DRAW_PROBABILITY_ONE", length = 5)
    public String getDrawProbabilityOne() {

        return this.drawProbabilityOne;
    }

    public void setDrawProbabilityOne(String drawProbabilityOne) {

        this.drawProbabilityOne = drawProbabilityOne.trim();
    }

    @Column(name = "WIN_PROBABILITY_TWO", length = 5)
    public String getWinProbabilityTwo() {

        return this.winProbabilityTwo;
    }

    public void setWinProbabilityTwo(String winProbabilityTwo) {

        this.winProbabilityTwo = winProbabilityTwo.trim();
    }

    @Column(name = "FAIL_PROBABILITY_TWO", length = 5)
    public String getFailProbabilityTwo() {

        return this.failProbabilityTwo;
    }

    public void setFailProbabilityTwo(String failProbabilityTwo) {

        this.failProbabilityTwo = failProbabilityTwo.trim();
    }

    @Column(name = "DRAW_PROBABILITY_TWO", length = 5)
    public String getDrawProbabilityTwo() {

        return this.drawProbabilityTwo;
    }

    public void setDrawProbabilityTwo(String drawProbabilityTwo) {

        this.drawProbabilityTwo = drawProbabilityTwo.trim();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME", length = 19)
    public Date getUpdateTime() {

        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {

        this.updateTime = updateTime;
    }

    public boolean validate() {

        boolean result = true;
        if ("".equals(competitionNum)) {
            result = false;
        }
        if ("".equals(startDate)) {
            result = false;
        }
        if ("".equals(BDate)) {
            result = false;
        }
        if ("".equals(homeTeam)) {
            result = false;
        }
        if ("".equals(visitionTeam)) {
            result = false;
        }
        if ("".equals(matchs)) {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((BDate == null) ? 0 : BDate.hashCode());
        result = prime * result + ((competitionNum == null) ? 0 : competitionNum.hashCode());
        result = prime * result + ((drawProbabilityOne == null) ? 0 : drawProbabilityOne.hashCode());
        result = prime * result + ((drawProbabilityTwo == null) ? 0 : drawProbabilityTwo.hashCode());
        result = prime * result + ((failProbabilityOne == null) ? 0 : failProbabilityOne.hashCode());
        result = prime * result + ((failProbabilityTwo == null) ? 0 : failProbabilityTwo.hashCode());
        result = prime * result + ((homeTeam == null) ? 0 : homeTeam.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
        result = prime * result + ((matchs == null) ? 0 : matchs.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((statusOne == null) ? 0 : statusOne.hashCode());
        result = prime * result + ((statusTwo == null) ? 0 : statusTwo.hashCode());
        result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
        result = prime * result + ((visitionTeam == null) ? 0 : visitionTeam.hashCode());
        result = prime * result + ((winCountOne == null) ? 0 : winCountOne.hashCode());
        result = prime * result + ((winCountTwo == null) ? 0 : winCountTwo.hashCode());
        result = prime * result + ((winProbabilityOne == null) ? 0 : winProbabilityOne.hashCode());
        result = prime * result + ((winProbabilityTwo == null) ? 0 : winProbabilityTwo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TCrawlerSportteryHistory other = (TCrawlerSportteryHistory) obj;
        if (BDate == null) {
            if (other.BDate != null)
                return false;
        } else if (!BDate.trim().equals(other.BDate.trim()))
            return false;
        if (competitionNum == null) {
            if (other.competitionNum != null)
                return false;
        } else if (!competitionNum.equals(other.competitionNum))
            return false;
        if (drawProbabilityOne == null) {
            if (other.drawProbabilityOne != null)
                return false;
        } else if (!drawProbabilityOne.equals(other.drawProbabilityOne))
            return false;
        if (drawProbabilityTwo == null) {
            if (other.drawProbabilityTwo != null)
                return false;
        } else if (!drawProbabilityTwo.equals(other.drawProbabilityTwo))
            return false;
        if (failProbabilityOne == null) {
            if (other.failProbabilityOne != null)
                return false;
        } else if (!failProbabilityOne.equals(other.failProbabilityOne))
            return false;
        if (failProbabilityTwo == null) {
            if (other.failProbabilityTwo != null)
                return false;
        } else if (!failProbabilityTwo.equals(other.failProbabilityTwo))
            return false;
        if (homeTeam == null) {
            if (other.homeTeam != null)
                return false;
        } else if (!homeTeam.equals(other.homeTeam))
            return false;
        if (matchs == null) {
            if (other.matchs != null)
                return false;
        } else if (!matchs.equals(other.matchs))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (statusOne == null) {
            if (other.statusOne != null)
                return false;
        } else if (!statusOne.equals(other.statusOne))
            return false;
        if (statusTwo == null) {
            if (other.statusTwo != null)
                return false;
        } else if (!statusTwo.equals(other.statusTwo))
            return false;
        if (visitionTeam == null) {
            if (other.visitionTeam != null)
                return false;
        } else if (!visitionTeam.equals(other.visitionTeam))
            return false;
        if (winCountOne == null) {
            if (other.winCountOne != null)
                return false;
        } else if (!winCountOne.equals(other.winCountOne))
            return false;
        if (winCountTwo == null) {
            if (other.winCountTwo != null)
                return false;
        } else if (!winCountTwo.equals(other.winCountTwo))
            return false;
        if (winProbabilityOne == null) {
            if (other.winProbabilityOne != null)
                return false;
        } else if (!winProbabilityOne.equals(other.winProbabilityOne))
            return false;
        if (winProbabilityTwo == null) {
            if (other.winProbabilityTwo != null)
                return false;
        } else if (!winProbabilityTwo.equals(other.winProbabilityTwo))
            return false;
        return true;
    }

    public TCrawlerSportteryHistory from(TCrawlerSporttery crawlerSporttery) {

        TCrawlerSportteryHistory crawlerSportteryHistory = new TCrawlerSportteryHistory();
        crawlerSportteryHistory.competitionNum = crawlerSporttery.getCompetitionNum();
        crawlerSportteryHistory.startDate = crawlerSporttery.getStartDateTime();
        crawlerSportteryHistory.BDate = crawlerSporttery.getBDate();
        crawlerSportteryHistory.lastUpdated = crawlerSporttery.getLastUpdated();
        crawlerSportteryHistory.status = crawlerSporttery.getStatus();
        crawlerSportteryHistory.matchs = crawlerSporttery.getMatchs();
        crawlerSportteryHistory.homeTeam = crawlerSporttery.getHomeTeam();
        crawlerSportteryHistory.visitionTeam = crawlerSporttery.getVisitionTeam();
        crawlerSportteryHistory.winCountOne = crawlerSporttery.getWinCountOne();
        crawlerSportteryHistory.statusOne = crawlerSporttery.getStatusOne();
        crawlerSportteryHistory.winCountTwo = crawlerSporttery.getWinCountTwo();
        crawlerSportteryHistory.statusTwo = crawlerSporttery.getStatusTwo();
        crawlerSportteryHistory.winProbabilityOne = crawlerSporttery.getWinProbabilityOne();
        crawlerSportteryHistory.failProbabilityOne = crawlerSporttery.getFailProbabilityOne();
        crawlerSportteryHistory.drawProbabilityOne = crawlerSporttery.getDrawProbabilityOne();
        crawlerSportteryHistory.winProbabilityTwo = crawlerSporttery.getWinProbabilityTwo();
        crawlerSportteryHistory.failProbabilityTwo = crawlerSporttery.getFailProbabilityTwo();
        crawlerSportteryHistory.drawProbabilityTwo = crawlerSporttery.getDrawProbabilityTwo();
        crawlerSportteryHistory.stopSaleTime = crawlerSporttery.getStopSaleTime();
        return crawlerSportteryHistory;
    }

    @Override
    public String toString() {

        return "TCrawlerSportteryHistory [id=" + id + ", competitionNum=" + competitionNum + ", startDateTime=" + startDate + ", BDate=" + BDate + ", lastUpdated=" + lastUpdated + ", status="
                + status + ", matchs=" + matchs + ", homeTeam=" + homeTeam + ", visitionTeam=" + visitionTeam + ", winCountOne=" + winCountOne + ", statusOne=" + statusOne + ", winCountTwo="
                + winCountTwo + ", statusTwo=" + statusTwo + ", winProbabilityOne=" + winProbabilityOne + ", failProbabilityOne=" + failProbabilityOne + ", drawProbabilityOne=" + drawProbabilityOne
                + ", winProbabilityTwo=" + winProbabilityTwo + ", failProbabilityTwo=" + failProbabilityTwo + ", drawProbabilityTwo=" + drawProbabilityTwo + ", updateTime=" + updateTime + "]";
    }
}
