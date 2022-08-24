package com.rampeo.tiw.progetto2022.DAOs;

import com.rampeo.tiw.progetto2022.Beans.MeetingBean;
import com.rampeo.tiw.progetto2022.Beans.UserBean;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class MeetingDAO extends AbstractDAO {
    public MeetingDAO() throws SQLException {
        super();
    }

    private List<MeetingBean> extractMeetings(String query, UserBean user) throws SQLException {
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
                        userBean.setEmail(result.getString("admin_email"));
                        meetingBean.setAdmin(userBean);
                        meetingBean.setId(result.getLong("id"));
                        meetingBean.setTitle(result.getString("title"));
                        Date startDate = result.getDate("date");
                        Time startTime = result.getTime("time");
                        meetingBean.setStart(startDate.toLocalDate().atTime(startTime.toLocalTime()).toInstant(ZoneOffset.UTC));
                        meetingBean.setDuration(result.getInt("duration"));
                        meetingBean.setCapacity(result.getInt("max_participants"));
                        meetingBeanList.add(meetingBean);
                    }
                    return List.copyOf(meetingBeanList);
                }
            }
        }
    }

    public List<MeetingBean> getCreatedMeetings(UserBean user) throws SQLException {
        final String query = "SELECT m.id, m.title, m.date, m.time, m.duration, m.max_participants, " +
                "m.admin AS admin_id, u.email as admin_email FROM meeting m " +
                "JOIN user u ON m.admin = u.id " +
                "WHERE u.id = ? AND ADDTIME(ADDTIME(m.date, m.time), SEC_TO_TIME(m.duration * 60)) >= UTC_TIMESTAMP()";
        return extractMeetings(query, user);
    }


    public List<MeetingBean> getInvitedMeetings(UserBean user) throws SQLException {
        final String query = "SELECT m.id, m.title, m.date, m.time, m.duration, m.max_participants, m.admin AS admin_id, u.email as admin_email " +
                "FROM meeting m JOIN meeting_invite mi ON m.id = mi.m_id " +
                "JOIN user u ON m.admin = u.id " +
                "WHERE mi.u_id = ? AND ADDTIME(ADDTIME(m.date, m.time), SEC_TO_TIME(m.duration * 60)) >= UTC_TIMESTAMP()";
        return extractMeetings(query, user);
    }

    public void createMeeting(MeetingBean meeting) throws SQLException {
        getConnection().setAutoCommit(false); // multiple queries happen in this function, ability to rollback is critical
        Connection con = getConnection();
        final String createMeeting = "INSERT INTO meeting (title, date, time, duration, max_participants, admin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        final String getMeetingID = "SELECT LAST_INSERT_ID()";
        final String createInvite = "INSERT INTO meeting_invite (m_id, u_id) VALUES (?, ?)";

        try {
            try (PreparedStatement statement = con.prepareStatement(createMeeting)) {
                statement.setString(1, meeting.getTitle());
                LocalDateTime start = LocalDateTime.ofInstant(meeting.getStart(), ZoneOffset.UTC);
                statement.setDate(2, Date.valueOf(start.toLocalDate()));
                statement.setTime(3, Time.valueOf(start.toLocalTime()));
                statement.setInt(4, meeting.getDuration());
                statement.setInt(5, meeting.getCapacity());
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
                for (Long userId : meeting.getInvites()) {
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
