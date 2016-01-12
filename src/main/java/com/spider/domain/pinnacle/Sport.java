package com.spider.domain.pinnacle;

/**
 * <sport id="1" feedContents="1">Badminton</sport>
 *
 * @author wsy
 */
public class Sport {

    Integer sportId;

    String name;

    public Sport() {

    }

    public Sport(Integer sportId, String name) {

        this.sportId = sportId;
        this.name = name;
    }

    public Integer getSportId() {

        return sportId;
    }

    public void setSportId(Integer sportId) {

        this.sportId = sportId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @Override
    public String toString() {

        return "Sport{" +
                "sportId=" + sportId +
                ", name='" + name + '\'' +
                '}';
    }
}
