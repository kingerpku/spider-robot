package com.spider.entity;

import javax.persistence.*;

/**
 * Created by wsy on 2015/12/23.
 */
@Entity
@Table(name = "nowgoal_event_type", schema = "", catalog = "crawler")
public class NowgoalEventTypeEntity {

    private long id;

    private String desc;

    private String image;

    @Id
    @Column(name = "id")
    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    @Basic
    @Column(name = "desc")
    public String getDesc() {

        return desc;
    }

    public void setDesc(String desc) {

        this.desc = desc;
    }

    @Basic
    @Column(name = "image")
    public String getImage() {

        return image;
    }

    public void setImage(String image) {

        this.image = image;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NowgoalEventTypeEntity that = (NowgoalEventTypeEntity) o;

        if (id != that.id) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}
