package com.spider.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by wsy on 2015/12/25.
 */
@Entity
@Table(name = "nowgoal_match", schema = "", catalog = "crawler")
public class NowgoalMatchEntity {

    private long id;

    private String matchTime;

    private String venue;

    private String weather;

    private String temperature;

    private String homeTeam;

    private String awayTeam;

    private Integer europeId;

    private Timestamp updateTime;

    @Id
    @Column(name = "id")
    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    @Basic
    @Column(name = "match_time")
    public String getMatchTime() {

        return matchTime;
    }

    public void setMatchTime(String matchTime) {

        this.matchTime = matchTime;
    }

    @Basic
    @Column(name = "venue")
    public String getVenue() {

        return venue;
    }

    public void setVenue(String venue) {

        this.venue = venue;
    }

    @Basic
    @Column(name = "weather")
    public String getWeather() {

        return weather;
    }

    public void setWeather(String weather) {

        this.weather = weather;
    }

    @Basic
    @Column(name = "temperature")
    public String getTemperature() {

        return temperature;
    }

    public void setTemperature(String temperature) {

        this.temperature = temperature;
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

    @Basic
    @Column(name = "europe_id")
    public Integer getEuropeId() {

        return europeId;
    }

    public void setEuropeId(Integer europeId) {

        this.europeId = europeId;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {

        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {

        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NowgoalMatchEntity that = (NowgoalMatchEntity) o;

        if (id != that.id) return false;
        if (matchTime != null ? !matchTime.equals(that.matchTime) : that.matchTime != null) return false;
        if (venue != null ? !venue.equals(that.venue) : that.venue != null) return false;
        if (weather != null ? !weather.equals(that.weather) : that.weather != null) return false;
        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null) return false;
        if (homeTeam != null ? !homeTeam.equals(that.homeTeam) : that.homeTeam != null) return false;
        if (awayTeam != null ? !awayTeam.equals(that.awayTeam) : that.awayTeam != null) return false;
        if (europeId != null ? !europeId.equals(that.europeId) : that.europeId != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (matchTime != null ? matchTime.hashCode() : 0);
        result = 31 * result + (venue != null ? venue.hashCode() : 0);
        result = 31 * result + (weather != null ? weather.hashCode() : 0);
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
        result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
        result = 31 * result + (europeId != null ? europeId.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
