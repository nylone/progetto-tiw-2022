package com.rampeo.tiw.progetto2022.Beans;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class MeetingBean {
    private Long id = null;
    private String title = null;
    private Long start = null;
    private Integer duration = null;
    private Integer capacity = null;
    private UserBean admin = null;

    public List<Long> getInvites() {
        return invites.stream().toList();
    }

    public void setInvites(Collection<Long> invites) {
        this.invites = invites;
    }

    private Collection<Long> invites = null;

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

    public Instant getStart() {
        return Instant.ofEpochMilli(start);
    }

    public void setStart(Instant start) {
        this.start = start.toEpochMilli();
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
