package com.rampeo.tiw.progetto2022.DAOs;

import com.rampeo.tiw.progetto2022.Beans.MeetingBean;
import com.rampeo.tiw.progetto2022.Beans.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeetingDAO extends AbstractDAO {
    public MeetingDAO() throws SQLException {
        super();
    }

    public List<MeetingBean> getCreatedMeetings(UserBean admin) throws SQLException {
        final String query = "SELECT m.id, m.title, m.date, m.time, m.duration, m.max_participants " +
                "FROM meeting m " +
                "JOIN user u ON m.admin = u.id " +
                "WHERE u.id = ? AND ADDTIME(ADDTIME(m.date, m.time), SEC_TO_TIME(m.duration * 60)) >= UTC_TIMESTAMP()";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setLong(1, admin.getId());
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst())
                    return List.of();
                else {
                    List<MeetingBean> meetingBeanList = new ArrayList<>();
                    while (result.next()) {
                        MeetingBean meetingBean = new MeetingBean();
                        meetingBean.setId(result.getLong("id"));
                        meetingBean.setTitle(result.getString("title"));
                        meetingBean.setDate(result.getDate("date"));
                        meetingBean.setTime(result.getTime("time"));
                        meetingBean.setDuration(result.getInt("duration"));
                        meetingBean.setMaxParticipants(result.getInt("max_participants"));
                        meetingBeanList.add(meetingBean);
                    }
                    return List.copyOf(meetingBeanList);
                }
            }
        }
    }


    public List<MeetingBean> getInvitedMeetings(UserBean user) throws SQLException {
        final String query = "SELECT m.id, m.title, m.date, m.time, m.duration, m.max_participants, m.admin AS admin_id, u.uname as admin_uname " +
                "FROM meeting m " +
                "JOIN meeting_invite mi ON m.id = mi.m_id " +
                "JOIN user u ON m.admin = u.id " +
                "WHERE mi.u_id = ? AND ADDTIME(ADDTIME(m.date, m.time), SEC_TO_TIME(m.duration * 60)) >= UTC_TIMESTAMP()";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setLong(1, user.getId());
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst())
                    return List.of();
                else {
                    List<MeetingBean> meetingBeanList = new ArrayList<>();
                    while (result.next()) {
                        MeetingBean meetingBean = new MeetingBean();
                        UserBean userBean = new UserBean();
                        userBean.setId(result.getLong("admin_id"));
                        userBean.setUname(result.getString("admin_uname"));
                        meetingBean.setAdmin(userBean);
                        meetingBean.setId(result.getLong("id"));
                        meetingBean.setTitle(result.getString("title"));
                        meetingBean.setDate(result.getDate("date"));
                        meetingBean.setTime(result.getTime("time"));
                        meetingBean.setDuration(result.getInt("duration"));
                        meetingBean.setMaxParticipants(result.getInt("max_participants"));
                        meetingBeanList.add(meetingBean);
                    }
                    return List.copyOf(meetingBeanList);
                }
            }
        }
    }

    public void createMeeting(MeetingBean meeting, List<Long> invites) throws SQLException {
        getConnection().setAutoCommit(false); // multiple queries happen in this function, ability to rollback is critical
        Connection con = getConnection();
        final String createMeeting = "INSERT INTO meeting (title, date, time, duration, max_participants, admin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        final String getMeetingID = "SELECT LAST_INSERT_ID()";
        final String createInvite = "INSERT INTO meeting_invite (m_id, u_id) VALUES (?, ?)";

        try {
            try (PreparedStatement statement = con.prepareStatement(createMeeting)) {
                statement.setString(1, meeting.getTitle());
                statement.setDate(2, meeting.getDate());
                statement.setTime(3, meeting.getTime());
                statement.setInt(4, meeting.getDuration());
                statement.setInt(5, meeting.getMaxParticipants());
                statement.setLong(6, meeting.getAdmin().getId());
                statement.execute();
            }

            try (PreparedStatement statement = con.prepareStatement(getMeetingID)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    meeting.setId(resultSet.getLong(1));
                }
            }

            try (PreparedStatement statement = con.prepareStatement(createInvite)) {
                for (Long userId : invites) {
                    statement.setLong(1, meeting.getId());
                    statement.setLong(2, userId);
                    statement.execute();
                }
            }

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
