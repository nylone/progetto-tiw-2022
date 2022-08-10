package com.rampeo.tiw.progetto2022.Beans;

import java.util.Set;

public class MeetingCreationBean {
    private MeetingBean meetingBean = null;
    private Integer failedAttempts = null;
    private Set<Long> selected = null;

    public MeetingBean getMeetingBean() {
        return meetingBean;
    }

    public void setMeetingBean(MeetingBean meetingBean) {
        this.meetingBean = meetingBean;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Set<Long> getSelected() {
        return selected;
    }

    public void setSelected(Set<Long> selected) {
        this.selected = selected;
    }
}
