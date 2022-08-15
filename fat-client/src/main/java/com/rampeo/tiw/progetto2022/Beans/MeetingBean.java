package com.rampeo.tiw.progetto2022.Beans;

import java.sql.Date;
import java.sql.Time;

public class MeetingBean {
    private Long id = null;
    private String title = null;
    private Date date = null;
    private Time time = null;
    private Integer duration = null;
    private Integer capacity = null;
    private UserBean admin = null;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public UserBean getAdmin() {
        return admin;
    }

    public void setAdmin(UserBean admin) {
        this.admin = admin;
    }
}
