package com.rampeo.tiw.progetto2022.Controllers;

import com.google.gson.Gson;
import com.rampeo.tiw.progetto2022.Beans.MeetingBean;
import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.DAOs.MeetingDAO;
import com.rampeo.tiw.progetto2022.Utils.Constants;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "getMeetings", urlPatterns = {Constants.GET_MEETINGS_ENDPOINT})
@MultipartConfig
public class GetMeetings extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final UserBean user = (UserBean) request.getSession().getAttribute(Constants.USER);
        List<MeetingBean> meetings;
        try (MeetingDAO meetingDAO = new MeetingDAO()) {
            switch (request.getParameter("type").trim().toLowerCase()) {
                case "created" -> meetings = meetingDAO.getCreatedMeetings(user);
                case "invited" -> meetings = meetingDAO.getInvitedMeetings(user);
                default -> {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(new Gson().toJson(meetings));
        out.flush();
    }
}
