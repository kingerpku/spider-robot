package com.spider.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by wsy on 2015/11/19.
 */
@Entity
@javax.persistence.Table(name = "sporttery_all_history")
public class SportteryAllHistoryEntity {

    private long id;

    private Long uniqueId;

    public SportteryAllHistoryEntity() {

    }

    public SportteryAllHistoryEntity(SportteryAllEntity s) {

        this.matchCode = s.getMatchCode();
        this.league = s.getLeague();
        this.homeTeam = s.getHomeTeam();
        this.awayTeam = s.getAwayTeam();
        this.hadH = s.getHadH();
        this.hadD = s.getHadD();
        this.hadA = s.getHadA();
        this.hhadH = s.getHhadH();
        this.hhadD = s.getHhadD();
        this.hhadA = s.getHhadA();
        this.hhadLine = s.getHhadLine();
        this.hafuHh = s.getHafuHh();
        this.hafuHd = s.getHafuHd();
        this.hafuHa = s.getHafuHa();
        this.hafuDh = s.getHafuDh();
        this.hafuDd = s.getHafuDd();
        this.hafuDa = s.getHafuDa();
        this.hafuAh = s.getHafuAh();
        this.hafuAd = s.getHafuAd();
        this.hafuAa = s.getHafuAa();
        this.ttg0 = s.getTtg0();
        this.ttg1 = s.getTtg1();
        this.ttg2 = s.getTtg2();
        this.ttg3 = s.getTtg3();
        this.ttg4 = s.getTtg4();
        this.ttg5 = s.getTtg5();
        this.ttg6 = s.getTtg6();
        this.ttg7Up = s.getTtg7Up();
        this.score10 = s.getScore10();
        this.score20 = s.getScore20();
        this.score21 = s.getScore21();
        this.score30 = s.getScore30();
        this.score31 = s.getScore31();
        this.score32 = s.getScore32();
        this.score40 = s.getScore40();
        this.score41 = s.getScore41();
        this.score42 = s.getScore42();
        this.score50 = s.getScore50();
        this.score51 = s.getScore51();
        this.score52 = s.getScore52();
        this.scoreHElse = s.getScoreHElse();
        this.score00 = s.getScore00();
        this.score11 = s.getScore11();
        this.score22 = s.getScore22();
        this.score33 = s.getScore33();
        this.scoreDElse = s.getScoreDElse();
        this.score01 = s.getScore01();
        this.score02 = s.getScore02();
        this.score12 = s.getScore12();
        this.score03 = s.getScore03();
        this.score13 = s.getScore13();
        this.score23 = s.getScore23();
        this.score04 = s.getScore04();
        this.score14 = s.getScore14();
        this.score24 = s.getScore24();
        this.score05 = s.getScore05();
        this.score15 = s.getScore15();
        this.score25 = s.getScore25();
        this.scoreAElse = s.getScoreAElse();
        this.startDateTime = s.getStartDateTime();
        this.uniqueId = s.getUniqueId();
        this.updateTime = s.getUpdateTime();
    }

    @Id
    @javax.persistence.Column(name = "id")
    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    private String matchCode;

    @Basic
    @javax.persistence.Column(name = "match_code")
    public String getMatchCode() {

        return matchCode;
    }

    public void setMatchCode(String matchCode) {

        this.matchCode = matchCode;
    }

    private String league;

    @Basic
    @javax.persistence.Column(name = "league")
    public String getLeague() {

        return league;
    }

    public void setLeague(String league) {

        this.league = league;
    }

    private String homeTeam;

    @Basic
    @javax.persistence.Column(name = "home_team")
    public String getHomeTeam() {

        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {

        this.homeTeam = homeTeam;
    }

    private String awayTeam;

    @Basic
    @javax.persistence.Column(name = "away_team")
    public String getAwayTeam() {

        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {

        this.awayTeam = awayTeam;
    }

    private Double hadH;

    @Basic
    @javax.persistence.Column(name = "had_h")
    public Double getHadH() {

        return hadH;
    }

    public void setHadH(Double hadH) {

        this.hadH = hadH;
    }

    private Double hadD;

    @Basic
    @javax.persistence.Column(name = "had_d")
    public Double getHadD() {

        return hadD;
    }

    public void setHadD(Double hadD) {

        this.hadD = hadD;
    }

    private Double hadA;

    @Basic
    @javax.persistence.Column(name = "had_a")
    public Double getHadA() {

        return hadA;
    }

    public void setHadA(Double hadA) {

        this.hadA = hadA;
    }

    private Double hhadH;

    @Basic
    @javax.persistence.Column(name = "hhad_h")
    public Double getHhadH() {

        return hhadH;
    }

    public void setHhadH(Double hhadH) {

        this.hhadH = hhadH;
    }

    private Double hhadD;

    @Basic
    @javax.persistence.Column(name = "hhad_d")
    public Double getHhadD() {

        return hhadD;
    }

    public void setHhadD(Double hhadD) {

        this.hhadD = hhadD;
    }

    private Double hhadA;

    @Basic
    @javax.persistence.Column(name = "hhad_a")
    public Double getHhadA() {

        return hhadA;
    }

    public void setHhadA(Double hhadA) {

        this.hhadA = hhadA;
    }

    private Double hhadLine;

    @Basic
    @javax.persistence.Column(name = "hhad_line")
    public Double getHhadLine() {

        return hhadLine;
    }

    public void setHhadLine(Double hhadLine) {

        this.hhadLine = hhadLine;
    }

    private Double hafuHh;

    @Basic
    @javax.persistence.Column(name = "hafu_hh")
    public Double getHafuHh() {

        return hafuHh;
    }

    public void setHafuHh(Double hafuHh) {

        this.hafuHh = hafuHh;
    }

    private Double hafuHd;

    @Basic
    @javax.persistence.Column(name = "hafu_hd")
    public Double getHafuHd() {

        return hafuHd;
    }

    public void setHafuHd(Double hafuHd) {

        this.hafuHd = hafuHd;
    }

    private Double hafuHa;

    @Basic
    @javax.persistence.Column(name = "hafu_ha")
    public Double getHafuHa() {

        return hafuHa;
    }

    public void setHafuHa(Double hafuHa) {

        this.hafuHa = hafuHa;
    }

    private Double hafuDh;

    @Basic
    @javax.persistence.Column(name = "hafu_dh")
    public Double getHafuDh() {

        return hafuDh;
    }

    public void setHafuDh(Double hafuDh) {

        this.hafuDh = hafuDh;
    }

    private Double hafuDd;

    @Basic
    @javax.persistence.Column(name = "hafu_dd")
    public Double getHafuDd() {

        return hafuDd;
    }

    public void setHafuDd(Double hafuDd) {

        this.hafuDd = hafuDd;
    }

    private Double hafuDa;

    @Basic
    @javax.persistence.Column(name = "hafu_da")
    public Double getHafuDa() {

        return hafuDa;
    }

    public void setHafuDa(Double hafuDa) {

        this.hafuDa = hafuDa;
    }

    private Double hafuAh;

    @Basic
    @javax.persistence.Column(name = "hafu_ah")
    public Double getHafuAh() {

        return hafuAh;
    }

    public void setHafuAh(Double hafuAh) {

        this.hafuAh = hafuAh;
    }

    private Double hafuAd;

    @Basic
    @javax.persistence.Column(name = "hafu_ad")
    public Double getHafuAd() {

        return hafuAd;
    }

    public void setHafuAd(Double hafuAd) {

        this.hafuAd = hafuAd;
    }

    private Double hafuAa;

    @Basic
    @javax.persistence.Column(name = "hafu_aa")
    public Double getHafuAa() {

        return hafuAa;
    }

    public void setHafuAa(Double hafuAa) {

        this.hafuAa = hafuAa;
    }

    private Double ttg0;

    @Basic
    @javax.persistence.Column(name = "ttg_0")
    public Double getTtg0() {

        return ttg0;
    }

    public void setTtg0(Double ttg0) {

        this.ttg0 = ttg0;
    }

    private Double ttg1;

    @Basic
    @javax.persistence.Column(name = "ttg_1")
    public Double getTtg1() {

        return ttg1;
    }

    public void setTtg1(Double ttg1) {

        this.ttg1 = ttg1;
    }

    private Double ttg2;

    @Basic
    @javax.persistence.Column(name = "ttg_2")
    public Double getTtg2() {

        return ttg2;
    }

    public void setTtg2(Double ttg2) {

        this.ttg2 = ttg2;
    }

    private Double ttg3;

    @Basic
    @javax.persistence.Column(name = "ttg_3")
    public Double getTtg3() {

        return ttg3;
    }

    public void setTtg3(Double ttg3) {

        this.ttg3 = ttg3;
    }

    private Double ttg4;

    @Basic
    @javax.persistence.Column(name = "ttg_4")
    public Double getTtg4() {

        return ttg4;
    }

    public void setTtg4(Double ttg4) {

        this.ttg4 = ttg4;
    }

    private Double ttg5;

    @Basic
    @javax.persistence.Column(name = "ttg_5")
    public Double getTtg5() {

        return ttg5;
    }

    public void setTtg5(Double ttg5) {

        this.ttg5 = ttg5;
    }

    private Double ttg6;

    @Basic
    @javax.persistence.Column(name = "ttg_6")
    public Double getTtg6() {

        return ttg6;
    }

    public void setTtg6(Double ttg6) {

        this.ttg6 = ttg6;
    }

    private Double ttg7Up;

    @Basic
    @javax.persistence.Column(name = "ttg_7up")
    public Double getTtg7Up() {

        return ttg7Up;
    }

    public void setTtg7Up(Double ttg7Up) {

        this.ttg7Up = ttg7Up;
    }

    private Double score10;

    @Basic
    @javax.persistence.Column(name = "score1_0")
    public Double getScore10() {

        return score10;
    }

    public void setScore10(Double score10) {

        this.score10 = score10;
    }

    private Double score20;

    @Basic
    @javax.persistence.Column(name = "score2_0")
    public Double getScore20() {

        return score20;
    }

    public void setScore20(Double score20) {

        this.score20 = score20;
    }

    private Double score21;

    @Basic
    @javax.persistence.Column(name = "score2_1")
    public Double getScore21() {

        return score21;
    }

    public void setScore21(Double score21) {

        this.score21 = score21;
    }

    private Double score30;

    @Basic
    @javax.persistence.Column(name = "score3_0")
    public Double getScore30() {

        return score30;
    }

    public void setScore30(Double score30) {

        this.score30 = score30;
    }

    private Double score31;

    @Basic
    @javax.persistence.Column(name = "score3_1")
    public Double getScore31() {

        return score31;
    }

    public void setScore31(Double score31) {

        this.score31 = score31;
    }

    private Double score32;

    @Basic
    @javax.persistence.Column(name = "score3_2")
    public Double getScore32() {

        return score32;
    }

    public void setScore32(Double score32) {

        this.score32 = score32;
    }

    private Double score40;

    @Basic
    @javax.persistence.Column(name = "score4_0")
    public Double getScore40() {

        return score40;
    }

    public void setScore40(Double score40) {

        this.score40 = score40;
    }

    private Double score41;

    @Basic
    @javax.persistence.Column(name = "score4_1")
    public Double getScore41() {

        return score41;
    }

    public void setScore41(Double score41) {

        this.score41 = score41;
    }

    private Double score42;

    @Basic
    @javax.persistence.Column(name = "score4_2")
    public Double getScore42() {

        return score42;
    }

    public void setScore42(Double score42) {

        this.score42 = score42;
    }

    private Double score50;

    @Basic
    @javax.persistence.Column(name = "score5_0")
    public Double getScore50() {

        return score50;
    }

    public void setScore50(Double score50) {

        this.score50 = score50;
    }

    private Double score51;

    @Basic
    @javax.persistence.Column(name = "score5_1")
    public Double getScore51() {

        return score51;
    }

    public void setScore51(Double score51) {

        this.score51 = score51;
    }

    private Double score52;

    @Basic
    @javax.persistence.Column(name = "score5_2")
    public Double getScore52() {

        return score52;
    }

    public void setScore52(Double score52) {

        this.score52 = score52;
    }

    private Double scoreHElse;

    @Basic
    @javax.persistence.Column(name = "score_h_else")
    public Double getScoreHElse() {

        return scoreHElse;
    }

    public void setScoreHElse(Double scoreHElse) {

        this.scoreHElse = scoreHElse;
    }

    private Double score00;

    @Basic
    @javax.persistence.Column(name = "score0_0")
    public Double getScore00() {

        return score00;
    }

    public void setScore00(Double score00) {

        this.score00 = score00;
    }

    private Double score11;

    @Basic
    @javax.persistence.Column(name = "score1_1")
    public Double getScore11() {

        return score11;
    }

    public void setScore11(Double score11) {

        this.score11 = score11;
    }

    private Double score22;

    @Basic
    @javax.persistence.Column(name = "score2_2")
    public Double getScore22() {

        return score22;
    }

    public void setScore22(Double score22) {

        this.score22 = score22;
    }

    private Double score33;

    @Basic
    @javax.persistence.Column(name = "score3_3")
    public Double getScore33() {

        return score33;
    }

    public void setScore33(Double score33) {

        this.score33 = score33;
    }

    private Double scoreDElse;

    @Basic
    @javax.persistence.Column(name = "score_d_else")
    public Double getScoreDElse() {

        return scoreDElse;
    }

    public void setScoreDElse(Double scoreDElse) {

        this.scoreDElse = scoreDElse;
    }

    private Double score01;

    @Basic
    @javax.persistence.Column(name = "score0_1")
    public Double getScore01() {

        return score01;
    }

    public void setScore01(Double score01) {

        this.score01 = score01;
    }

    private Double score02;

    @Basic
    @javax.persistence.Column(name = "score0_2")
    public Double getScore02() {

        return score02;
    }

    public void setScore02(Double score02) {

        this.score02 = score02;
    }

    private Double score12;

    @Basic
    @javax.persistence.Column(name = "score1_2")
    public Double getScore12() {

        return score12;
    }

    public void setScore12(Double score12) {

        this.score12 = score12;
    }

    private Double score03;

    @Basic
    @javax.persistence.Column(name = "score0_3")
    public Double getScore03() {

        return score03;
    }

    public void setScore03(Double score03) {

        this.score03 = score03;
    }

    private Double score13;

    @Basic
    @javax.persistence.Column(name = "score1_3")
    public Double getScore13() {

        return score13;
    }

    public void setScore13(Double score13) {

        this.score13 = score13;
    }

    private Double score23;

    @Basic
    @javax.persistence.Column(name = "score2_3")
    public Double getScore23() {

        return score23;
    }

    public void setScore23(Double score23) {

        this.score23 = score23;
    }

    private Double score04;

    @Basic
    @javax.persistence.Column(name = "score0_4")
    public Double getScore04() {

        return score04;
    }

    public void setScore04(Double score04) {

        this.score04 = score04;
    }

    private Double score14;

    @Basic
    @javax.persistence.Column(name = "score1_4")
    public Double getScore14() {

        return score14;
    }

    public void setScore14(Double score14) {

        this.score14 = score14;
    }

    private Double score24;

    @Basic
    @javax.persistence.Column(name = "score2_4")
    public Double getScore24() {

        return score24;
    }

    public void setScore24(Double score24) {

        this.score24 = score24;
    }

    private Double score05;

    @Basic
    @javax.persistence.Column(name = "score0_5")
    public Double getScore05() {

        return score05;
    }

    public void setScore05(Double score05) {

        this.score05 = score05;
    }

    private Double score15;

    @Basic
    @javax.persistence.Column(name = "score1_5")
    public Double getScore15() {

        return score15;
    }

    public void setScore15(Double score15) {

        this.score15 = score15;
    }

    private Double score25;

    @Basic
    @javax.persistence.Column(name = "score2_5")
    public Double getScore25() {

        return score25;
    }

    public void setScore25(Double score25) {

        this.score25 = score25;
    }

    private Double scoreAElse;

    @Basic
    @javax.persistence.Column(name = "score_a_else")
    public Double getScoreAElse() {

        return scoreAElse;
    }

    public void setScoreAElse(Double scoreAElse) {

        this.scoreAElse = scoreAElse;
    }

    private Timestamp startDateTime;

    @Basic
    @javax.persistence.Column(name = "start_date_time")
    public Timestamp getStartDateTime() {

        return startDateTime;
    }

    public void setStartDateTime(Timestamp oddsUpdateTime) {

        this.startDateTime = oddsUpdateTime;
    }

    private Timestamp updateTime;

    @Basic
    @javax.persistence.Column(name = "update_time")
    public Timestamp getUpdateTime() {

        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {

        this.updateTime = updateTime;
    }

    public void setUniqueId(Long uniqueId) {

        this.uniqueId = uniqueId;
    }

    @Basic
    @Column(name = "unique_id")
    public Long getUniqueId() {

        return uniqueId;
    }
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SportteryAllHistoryEntity that = (SportteryAllHistoryEntity) o;

        if (id != that.id) return false;
        if (matchCode != null ? !matchCode.equals(that.matchCode) : that.matchCode != null) return false;
        if (league != null ? !league.equals(that.league) : that.league != null) return false;
        if (homeTeam != null ? !homeTeam.equals(that.homeTeam) : that.homeTeam != null) return false;
        if (awayTeam != null ? !awayTeam.equals(that.awayTeam) : that.awayTeam != null) return false;
        if (hadH != null ? !hadH.equals(that.hadH) : that.hadH != null) return false;
        if (hadD != null ? !hadD.equals(that.hadD) : that.hadD != null) return false;
        if (hadA != null ? !hadA.equals(that.hadA) : that.hadA != null) return false;
        if (hhadH != null ? !hhadH.equals(that.hhadH) : that.hhadH != null) return false;
        if (hhadD != null ? !hhadD.equals(that.hhadD) : that.hhadD != null) return false;
        if (hhadA != null ? !hhadA.equals(that.hhadA) : that.hhadA != null) return false;
        if (hhadLine != null ? !hhadLine.equals(that.hhadLine) : that.hhadLine != null) return false;
        if (hafuHh != null ? !hafuHh.equals(that.hafuHh) : that.hafuHh != null) return false;
        if (hafuHd != null ? !hafuHd.equals(that.hafuHd) : that.hafuHd != null) return false;
        if (hafuHa != null ? !hafuHa.equals(that.hafuHa) : that.hafuHa != null) return false;
        if (hafuDh != null ? !hafuDh.equals(that.hafuDh) : that.hafuDh != null) return false;
        if (hafuDd != null ? !hafuDd.equals(that.hafuDd) : that.hafuDd != null) return false;
        if (hafuDa != null ? !hafuDa.equals(that.hafuDa) : that.hafuDa != null) return false;
        if (hafuAh != null ? !hafuAh.equals(that.hafuAh) : that.hafuAh != null) return false;
        if (hafuAd != null ? !hafuAd.equals(that.hafuAd) : that.hafuAd != null) return false;
        if (hafuAa != null ? !hafuAa.equals(that.hafuAa) : that.hafuAa != null) return false;
        if (ttg0 != null ? !ttg0.equals(that.ttg0) : that.ttg0 != null) return false;
        if (ttg1 != null ? !ttg1.equals(that.ttg1) : that.ttg1 != null) return false;
        if (ttg2 != null ? !ttg2.equals(that.ttg2) : that.ttg2 != null) return false;
        if (ttg3 != null ? !ttg3.equals(that.ttg3) : that.ttg3 != null) return false;
        if (ttg4 != null ? !ttg4.equals(that.ttg4) : that.ttg4 != null) return false;
        if (ttg5 != null ? !ttg5.equals(that.ttg5) : that.ttg5 != null) return false;
        if (ttg6 != null ? !ttg6.equals(that.ttg6) : that.ttg6 != null) return false;
        if (ttg7Up != null ? !ttg7Up.equals(that.ttg7Up) : that.ttg7Up != null) return false;
        if (score10 != null ? !score10.equals(that.score10) : that.score10 != null) return false;
        if (score20 != null ? !score20.equals(that.score20) : that.score20 != null) return false;
        if (score21 != null ? !score21.equals(that.score21) : that.score21 != null) return false;
        if (score30 != null ? !score30.equals(that.score30) : that.score30 != null) return false;
        if (score31 != null ? !score31.equals(that.score31) : that.score31 != null) return false;
        if (score32 != null ? !score32.equals(that.score32) : that.score32 != null) return false;
        if (score40 != null ? !score40.equals(that.score40) : that.score40 != null) return false;
        if (score41 != null ? !score41.equals(that.score41) : that.score41 != null) return false;
        if (score42 != null ? !score42.equals(that.score42) : that.score42 != null) return false;
        if (score50 != null ? !score50.equals(that.score50) : that.score50 != null) return false;
        if (score51 != null ? !score51.equals(that.score51) : that.score51 != null) return false;
        if (score52 != null ? !score52.equals(that.score52) : that.score52 != null) return false;
        if (scoreHElse != null ? !scoreHElse.equals(that.scoreHElse) : that.scoreHElse != null) return false;
        if (score00 != null ? !score00.equals(that.score00) : that.score00 != null) return false;
        if (score11 != null ? !score11.equals(that.score11) : that.score11 != null) return false;
        if (score22 != null ? !score22.equals(that.score22) : that.score22 != null) return false;
        if (score33 != null ? !score33.equals(that.score33) : that.score33 != null) return false;
        if (scoreDElse != null ? !scoreDElse.equals(that.scoreDElse) : that.scoreDElse != null) return false;
        if (score01 != null ? !score01.equals(that.score01) : that.score01 != null) return false;
        if (score02 != null ? !score02.equals(that.score02) : that.score02 != null) return false;
        if (score12 != null ? !score12.equals(that.score12) : that.score12 != null) return false;
        if (score03 != null ? !score03.equals(that.score03) : that.score03 != null) return false;
        if (score13 != null ? !score13.equals(that.score13) : that.score13 != null) return false;
        if (score23 != null ? !score23.equals(that.score23) : that.score23 != null) return false;
        if (score04 != null ? !score04.equals(that.score04) : that.score04 != null) return false;
        if (score14 != null ? !score14.equals(that.score14) : that.score14 != null) return false;
        if (score24 != null ? !score24.equals(that.score24) : that.score24 != null) return false;
        if (score05 != null ? !score05.equals(that.score05) : that.score05 != null) return false;
        if (score15 != null ? !score15.equals(that.score15) : that.score15 != null) return false;
        if (score25 != null ? !score25.equals(that.score25) : that.score25 != null) return false;
        if (uniqueId != null ? !uniqueId.equals(that.uniqueId) : that.uniqueId != null) return false;
        if (scoreAElse != null ? !scoreAElse.equals(that.scoreAElse) : that.scoreAElse != null) return false;
        if (startDateTime != null ? !startDateTime.equals(that.startDateTime) : that.startDateTime != null)
            return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (matchCode != null ? matchCode.hashCode() : 0);
        result = 31 * result + (league != null ? league.hashCode() : 0);
        result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
        result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
        result = 31 * result + (hadH != null ? hadH.hashCode() : 0);
        result = 31 * result + (hadD != null ? hadD.hashCode() : 0);
        result = 31 * result + (hadA != null ? hadA.hashCode() : 0);
        result = 31 * result + (hhadH != null ? hhadH.hashCode() : 0);
        result = 31 * result + (hhadD != null ? hhadD.hashCode() : 0);
        result = 31 * result + (hhadA != null ? hhadA.hashCode() : 0);
        result = 31 * result + (hhadLine != null ? hhadLine.hashCode() : 0);
        result = 31 * result + (hafuHh != null ? hafuHh.hashCode() : 0);
        result = 31 * result + (hafuHd != null ? hafuHd.hashCode() : 0);
        result = 31 * result + (hafuHa != null ? hafuHa.hashCode() : 0);
        result = 31 * result + (hafuDh != null ? hafuDh.hashCode() : 0);
        result = 31 * result + (hafuDd != null ? hafuDd.hashCode() : 0);
        result = 31 * result + (hafuDa != null ? hafuDa.hashCode() : 0);
        result = 31 * result + (hafuAh != null ? hafuAh.hashCode() : 0);
        result = 31 * result + (hafuAd != null ? hafuAd.hashCode() : 0);
        result = 31 * result + (hafuAa != null ? hafuAa.hashCode() : 0);
        result = 31 * result + (ttg0 != null ? ttg0.hashCode() : 0);
        result = 31 * result + (ttg1 != null ? ttg1.hashCode() : 0);
        result = 31 * result + (ttg2 != null ? ttg2.hashCode() : 0);
        result = 31 * result + (ttg3 != null ? ttg3.hashCode() : 0);
        result = 31 * result + (ttg4 != null ? ttg4.hashCode() : 0);
        result = 31 * result + (ttg5 != null ? ttg5.hashCode() : 0);
        result = 31 * result + (ttg6 != null ? ttg6.hashCode() : 0);
        result = 31 * result + (ttg7Up != null ? ttg7Up.hashCode() : 0);
        result = 31 * result + (score10 != null ? score10.hashCode() : 0);
        result = 31 * result + (score20 != null ? score20.hashCode() : 0);
        result = 31 * result + (score21 != null ? score21.hashCode() : 0);
        result = 31 * result + (score30 != null ? score30.hashCode() : 0);
        result = 31 * result + (score31 != null ? score31.hashCode() : 0);
        result = 31 * result + (score32 != null ? score32.hashCode() : 0);
        result = 31 * result + (score40 != null ? score40.hashCode() : 0);
        result = 31 * result + (score41 != null ? score41.hashCode() : 0);
        result = 31 * result + (score42 != null ? score42.hashCode() : 0);
        result = 31 * result + (score50 != null ? score50.hashCode() : 0);
        result = 31 * result + (score51 != null ? score51.hashCode() : 0);
        result = 31 * result + (score52 != null ? score52.hashCode() : 0);
        result = 31 * result + (scoreHElse != null ? scoreHElse.hashCode() : 0);
        result = 31 * result + (score00 != null ? score00.hashCode() : 0);
        result = 31 * result + (score11 != null ? score11.hashCode() : 0);
        result = 31 * result + (score22 != null ? score22.hashCode() : 0);
        result = 31 * result + (score33 != null ? score33.hashCode() : 0);
        result = 31 * result + (scoreDElse != null ? scoreDElse.hashCode() : 0);
        result = 31 * result + (score01 != null ? score01.hashCode() : 0);
        result = 31 * result + (score02 != null ? score02.hashCode() : 0);
        result = 31 * result + (score12 != null ? score12.hashCode() : 0);
        result = 31 * result + (score03 != null ? score03.hashCode() : 0);
        result = 31 * result + (score13 != null ? score13.hashCode() : 0);
        result = 31 * result + (score23 != null ? score23.hashCode() : 0);
        result = 31 * result + (score04 != null ? score04.hashCode() : 0);
        result = 31 * result + (score14 != null ? score14.hashCode() : 0);
        result = 31 * result + (score24 != null ? score24.hashCode() : 0);
        result = 31 * result + (score05 != null ? score05.hashCode() : 0);
        result = 31 * result + (score15 != null ? score15.hashCode() : 0);
        result = 31 * result + (score25 != null ? score25.hashCode() : 0);
        result = 31 * result + (scoreAElse != null ? scoreAElse.hashCode() : 0);
        result = 31 * result + (startDateTime != null ? startDateTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "SportteryAllHistoryEntity{" +
                "id=" + id +
                ", uniqueId=" + uniqueId +
                ", matchCode='" + matchCode + '\'' +
                ", league='" + league + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", hadH=" + hadH +
                ", hadD=" + hadD +
                ", hadA=" + hadA +
                ", hhadH=" + hhadH +
                ", hhadD=" + hhadD +
                ", hhadA=" + hhadA +
                ", hhadLine=" + hhadLine +
                ", hafuHh=" + hafuHh +
                ", hafuHd=" + hafuHd +
                ", hafuHa=" + hafuHa +
                ", hafuDh=" + hafuDh +
                ", hafuDd=" + hafuDd +
                ", hafuDa=" + hafuDa +
                ", hafuAh=" + hafuAh +
                ", hafuAd=" + hafuAd +
                ", hafuAa=" + hafuAa +
                ", ttg0=" + ttg0 +
                ", ttg1=" + ttg1 +
                ", ttg2=" + ttg2 +
                ", ttg3=" + ttg3 +
                ", ttg4=" + ttg4 +
                ", ttg5=" + ttg5 +
                ", ttg6=" + ttg6 +
                ", ttg7Up=" + ttg7Up +
                ", score10=" + score10 +
                ", score20=" + score20 +
                ", score21=" + score21 +
                ", score30=" + score30 +
                ", score31=" + score31 +
                ", score32=" + score32 +
                ", score40=" + score40 +
                ", score41=" + score41 +
                ", score42=" + score42 +
                ", score50=" + score50 +
                ", score51=" + score51 +
                ", score52=" + score52 +
                ", scoreHElse=" + scoreHElse +
                ", score00=" + score00 +
                ", score11=" + score11 +
                ", score22=" + score22 +
                ", score33=" + score33 +
                ", scoreDElse=" + scoreDElse +
                ", score01=" + score01 +
                ", score02=" + score02 +
                ", score12=" + score12 +
                ", score03=" + score03 +
                ", score13=" + score13 +
                ", score23=" + score23 +
                ", score04=" + score04 +
                ", score14=" + score14 +
                ", score24=" + score24 +
                ", score05=" + score05 +
                ", score15=" + score15 +
                ", score25=" + score25 +
                ", scoreAElse=" + scoreAElse +
                ", startDateTime=" + startDateTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
