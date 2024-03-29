package com.spider.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by wsy on 2015/12/25.
 */
@Entity
@Table(name = "nowgoal_match_players", schema = "", catalog = "crawler")
public class NowgoalMatchPlayersEntity {

    private long id;

    private long matchId;

    private String player;

    private Boolean isFirst;

    private Integer team;

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
    @Column(name = "match_id")
    public long getMatchId() {

        return matchId;
    }

    public void setMatchId(long matchId) {

        this.matchId = matchId;
    }

    @Basic
    @Column(name = "player")
    public String getPlayer() {

        return player;
    }

    public void setPlayer(String player) {

        this.player = player;
    }

    @Basic
    @Column(name = "is_first")
    public Boolean getIsFirst() {

        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {

        this.isFirst = isFirst;
    }

    @Basic
    @Column(name = "team")
    public Integer getTeam() {

        return team;
    }

    public void setTeam(Integer team) {

        this.team = team;
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

        NowgoalMatchPlayersEntity that = (NowgoalMatchPlayersEntity) o;

        if (id != that.id) return false;
        if (matchId != that.matchId) return false;
        if (player != null ? !player.equals(that.player) : that.player != null) return false;
        if (isFirst != null ? !isFirst.equals(that.isFirst) : that.isFirst != null) return false;
        if (team != null ? !team.equals(that.team) : that.team != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (matchId ^ (matchId >>> 32));
        result = 31 * result + (player != null ? player.hashCode() : 0);
        result = 31 * result + (isFirst != null ? isFirst.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
