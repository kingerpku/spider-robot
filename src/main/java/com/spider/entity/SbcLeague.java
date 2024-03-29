package com.spider.entity;

// Generated 2015-7-14 17:26:36 by Hibernate Tools 4.3.1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SbcLeague generated by hbm2java
 */
@Entity
@Table(name = "sbc_league")
public class SbcLeague implements java.io.Serializable {

    private static final long serialVersionUID = 2174654923280095590L;

    private Long id;

    private String leagueNameEnglish;

    private String leagueNameAbbr;

    private String leagueNameChinese;

    private String sportteryName;

    private String win310Name;

    private String win310PinnaName;

    private String pinnaclesportsName;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @Column(name = "LEAGUE_NAME_ENGLISH", length = 100)
    public String getLeagueNameEnglish() {

        return this.leagueNameEnglish;
    }

    public void setLeagueNameEnglish(String leagueNameEnglish) {

        this.leagueNameEnglish = leagueNameEnglish;
    }

    @Column(name = "LEAGUE_NAME_ABBR", length = 100)
    public String getLeagueNameAbbr() {

        return this.leagueNameAbbr;
    }

    public void setLeagueNameAbbr(String leagueNameAbbr) {

        this.leagueNameAbbr = leagueNameAbbr;
    }

    @Column(name = "LEAGUE_NAME_CHINESE", length = 100)
    public String getLeagueNameChinese() {

        return this.leagueNameChinese;
    }

    public void setLeagueNameChinese(String leagueNameChinese) {

        this.leagueNameChinese = leagueNameChinese;
    }

    @Column(name = "SPORTTERY_NAME", length = 100)
    public String getSportteryName() {

        return this.sportteryName;
    }

    public void setSportteryName(String sportteryName) {

        this.sportteryName = sportteryName;
    }

    @Column(name = "WIN310_NAME", length = 100)
    public String getWin310Name() {

        return this.win310Name;
    }

    public void setWin310Name(String win310Name) {

        this.win310Name = win310Name;
    }

    @Column(name = "OTHER_NAME", length = 100)
    public String getWin310PinnaName() {

        return this.win310PinnaName;
    }

    public void setWin310PinnaName(String win310PinnaName) {

        this.win310PinnaName = win310PinnaName;
    }

    @Column(name = "PINNACLESPORTS_NAME", length = 100)
    public String getPinnaclesportsName() {

        return this.pinnaclesportsName;
    }

    public void setPinnaclesportsName(String pinnaclesportsName) {

        this.pinnaclesportsName = pinnaclesportsName;
    }
}
