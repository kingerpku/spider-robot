package com.spider.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by wsy on 2015/12/21.
 */
@Entity
@Table(name = "pinnacle_history", schema = "", catalog = "crawler")
public class PinnacleHistoryEntity {

    private  long id;
    private long eventId;

    private Integer leagueId;

    private Timestamp startDateTime;

    private Timestamp cutOffDateTime;

    private Double awaySpread;

    private BigDecimal awayPrice;

    private Double homeSpread;

    private BigDecimal homePrice;

    private Boolean isLive;

    private Timestamp updateTime;

    private String homeTeam;

    private String awayTeam;

    public PinnacleHistoryEntity(PinnacleEntity pinnacleEntity) {

        this.eventId = pinnacleEntity.getEventId();
        this.leagueId = pinnacleEntity.getLeagueId();
        this.startDateTime = pinnacleEntity.getStartDateTime();
        this.cutOffDateTime = pinnacleEntity.getCutOffDateTime();
        this.awaySpread = pinnacleEntity.getAwaySpread();
        this.awayPrice = pinnacleEntity.getAwayPrice();
        this.homeSpread = pinnacleEntity.getHomeSpread();
        this.homePrice = pinnacleEntity.getHomePrice();
        this.isLive = pinnacleEntity.getIsLive();
        this.updateTime = pinnacleEntity.getUpdateTime();
        this.homeTeam = pinnacleEntity.getHomeTeam();
        this.awayTeam = pinnacleEntity.getAwayTeam();
    }

    public PinnacleHistoryEntity() {

    }
    @Id
    @GeneratedValue
    @Column(name = "id")
    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    @Basic
    @Column(name = "event_id")
    public long getEventId() {

        return eventId;
    }

    public void setEventId(long eventId) {

        this.eventId = eventId;
    }

    @Basic
    @Column(name = "league_id")
    public Integer getLeagueId() {

        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {

        this.leagueId = leagueId;
    }

    @Basic
    @Column(name = "start_date_time")
    public Timestamp getStartDateTime() {

        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {

        this.startDateTime = startDateTime;
    }

    @Basic
    @Column(name = "cut_off_date_time")
    public Timestamp getCutOffDateTime() {

        return cutOffDateTime;
    }

    public void setCutOffDateTime(Timestamp cutOffDateTime) {

        this.cutOffDateTime = cutOffDateTime;
    }

    @Basic
    @Column(name = "away_spread")
    public Double getAwaySpread() {

        return awaySpread;
    }

    public void setAwaySpread(Double awaySpread) {

        this.awaySpread = awaySpread;
    }

    @Basic
    @Column(name = "away_price")
    public BigDecimal getAwayPrice() {

        return awayPrice;
    }

    public void setAwayPrice(BigDecimal awayPrice) {

        this.awayPrice = awayPrice;
    }

    @Basic
    @Column(name = "home_spread")
    public Double getHomeSpread() {

        return homeSpread;
    }

    public void setHomeSpread(Double homeSpread) {

        this.homeSpread = homeSpread;
    }

    @Basic
    @Column(name = "home_price")
    public BigDecimal getHomePrice() {

        return homePrice;
    }

    public void setHomePrice(BigDecimal homePrice) {

        this.homePrice = homePrice;
    }

    @Basic
    @Column(name = "is_live")
    public Boolean getIsLive() {

        return isLive;
    }

    public void setIsLive(Boolean isLive) {

        this.isLive = isLive;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {

        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {

        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "home_team")
    public String getHomeTeam() {

        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {

        this.homeTeam = homeTeam;
    }

    @Basic
    @Column(name = "away_team")
    public String getAwayTeam() {

        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {

        this.awayTeam = awayTeam;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PinnacleHistoryEntity that = (PinnacleHistoryEntity) o;

        if (eventId != that.eventId) return false;
        if (leagueId != null ? !leagueId.equals(that.leagueId) : that.leagueId != null) return false;
        if (startDateTime != null ? !startDateTime.equals(that.startDateTime) : that.startDateTime != null)
            return false;
        if (cutOffDateTime != null ? !cutOffDateTime.equals(that.cutOffDateTime) : that.cutOffDateTime != null)
            return false;
        if (awaySpread != null ? !awaySpread.equals(that.awaySpread) : that.awaySpread != null) return false;
        if (awayPrice != null ? !awayPrice.equals(that.awayPrice) : that.awayPrice != null) return false;
        if (homeSpread != null ? !homeSpread.equals(that.homeSpread) : that.homeSpread != null) return false;
        if (homePrice != null ? !homePrice.equals(that.homePrice) : that.homePrice != null) return false;
        if (isLive != null ? !isLive.equals(that.isLive) : that.isLive != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (homeTeam != null ? !homeTeam.equals(that.homeTeam) : that.homeTeam != null) return false;
        if (awayTeam != null ? !awayTeam.equals(that.awayTeam) : that.awayTeam != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (leagueId != null ? leagueId.hashCode() : 0);
        result = 31 * result + (startDateTime != null ? startDateTime.hashCode() : 0);
        result = 31 * result + (cutOffDateTime != null ? cutOffDateTime.hashCode() : 0);
        result = 31 * result + (awaySpread != null ? awaySpread.hashCode() : 0);
        result = 31 * result + (awayPrice != null ? awayPrice.hashCode() : 0);
        result = 31 * result + (homeSpread != null ? homeSpread.hashCode() : 0);
        result = 31 * result + (homePrice != null ? homePrice.hashCode() : 0);
        result = 31 * result + (isLive != null ? isLive.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
        result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "PinnacleHistoryEntity{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", leagueId=" + leagueId +
                ", startDateTime=" + startDateTime +
                ", cutOffDateTime=" + cutOffDateTime +
                ", awaySpread=" + awaySpread +
                ", awayPrice=" + awayPrice +
                ", homeSpread=" + homeSpread +
                ", homePrice=" + homePrice +
                ", isLive=" + isLive +
                ", updateTime=" + updateTime +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                '}';
    }
}
