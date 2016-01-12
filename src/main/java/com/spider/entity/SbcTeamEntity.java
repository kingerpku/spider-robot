package com.spider.entity;

import javax.persistence.*;

/**
 * Created by wsy on 2015/12/21.
 */
@Entity
@Table(name = "sbc_team", schema = "", catalog = "crawler")
public class SbcTeamEntity {

    private long id;

    private String pinnacleName;

    private String pinnacleNameEng;

    private String sportteryName;

    private String sportteryNameEng;

    private String win310Name;

    private String win310NameEng;

    private String teamNameAbbr;

    private String teamNameEngAbbr;

    @Id
    @Column(name = "ID")
    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    @Basic
    @Column(name = "PINNACLE_NAME")
    public String getPinnacleName() {

        return pinnacleName;
    }

    public void setPinnacleName(String pinnacleName) {

        this.pinnacleName = pinnacleName;
    }

    @Basic
    @Column(name = "PINNACLE_NAME_ENG")
    public String getPinnacleNameEng() {

        return pinnacleNameEng;
    }

    public void setPinnacleNameEng(String pinnacleNameEng) {

        this.pinnacleNameEng = pinnacleNameEng;
    }

    @Basic
    @Column(name = "SPORTTERY_NAME")
    public String getSportteryName() {

        return sportteryName;
    }

    public void setSportteryName(String sportteryName) {

        this.sportteryName = sportteryName;
    }

    @Basic
    @Column(name = "SPORTTERY_NAME_ENG")
    public String getSportteryNameEng() {

        return sportteryNameEng;
    }

    public void setSportteryNameEng(String sportteryNameEng) {

        this.sportteryNameEng = sportteryNameEng;
    }

    @Basic
    @Column(name = "WIN310_NAME")
    public String getWin310Name() {

        return win310Name;
    }

    public void setWin310Name(String win310Name) {

        this.win310Name = win310Name;
    }

    @Basic
    @Column(name = "WIN310_NAME_ENG")
    public String getWin310NameEng() {

        return win310NameEng;
    }

    public void setWin310NameEng(String win310NameEng) {

        this.win310NameEng = win310NameEng;
    }

    @Basic
    @Column(name = "TEAM_NAME_ABBR")
    public String getTeamNameAbbr() {

        return teamNameAbbr;
    }

    public void setTeamNameAbbr(String teamNameAbbr) {

        this.teamNameAbbr = teamNameAbbr;
    }

    @Basic
    @Column(name = "TEAM_NAME_ENG_ABBR")
    public String getTeamNameEngAbbr() {

        return teamNameEngAbbr;
    }

    public void setTeamNameEngAbbr(String teamNameEngAbbr) {

        this.teamNameEngAbbr = teamNameEngAbbr;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SbcTeamEntity that = (SbcTeamEntity) o;

        if (id != that.id) return false;
        if (pinnacleName != null ? !pinnacleName.equals(that.pinnacleName) : that.pinnacleName != null) return false;
        if (pinnacleNameEng != null ? !pinnacleNameEng.equals(that.pinnacleNameEng) : that.pinnacleNameEng != null)
            return false;
        if (sportteryName != null ? !sportteryName.equals(that.sportteryName) : that.sportteryName != null)
            return false;
        if (sportteryNameEng != null ? !sportteryNameEng.equals(that.sportteryNameEng) : that.sportteryNameEng != null)
            return false;
        if (win310Name != null ? !win310Name.equals(that.win310Name) : that.win310Name != null) return false;
        if (win310NameEng != null ? !win310NameEng.equals(that.win310NameEng) : that.win310NameEng != null)
            return false;
        if (teamNameAbbr != null ? !teamNameAbbr.equals(that.teamNameAbbr) : that.teamNameAbbr != null) return false;
        if (teamNameEngAbbr != null ? !teamNameEngAbbr.equals(that.teamNameEngAbbr) : that.teamNameEngAbbr != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (pinnacleName != null ? pinnacleName.hashCode() : 0);
        result = 31 * result + (pinnacleNameEng != null ? pinnacleNameEng.hashCode() : 0);
        result = 31 * result + (sportteryName != null ? sportteryName.hashCode() : 0);
        result = 31 * result + (sportteryNameEng != null ? sportteryNameEng.hashCode() : 0);
        result = 31 * result + (win310Name != null ? win310Name.hashCode() : 0);
        result = 31 * result + (win310NameEng != null ? win310NameEng.hashCode() : 0);
        result = 31 * result + (teamNameAbbr != null ? teamNameAbbr.hashCode() : 0);
        result = 31 * result + (teamNameEngAbbr != null ? teamNameEngAbbr.hashCode() : 0);
        return result;
    }
}
