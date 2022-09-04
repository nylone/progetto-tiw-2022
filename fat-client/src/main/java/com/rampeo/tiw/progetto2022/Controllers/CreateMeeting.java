package com.rampeo.tiw.progetto2022.Controllers;

import com.rampeo.tiw.progetto2022.Beans.MeetingBean;
import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.DAOs.MeetingDAO;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;
import com.rampeo.tiw.progetto2022.Utils.Constants;
import com.rampeo.tiw.progetto2022.Utils.HTTPParameterChecker;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "CreateMeeting", urlPatterns = {Constants.CREATE_MEETING_ENDPOINT})
@MultipartConfig
public class CreateMeeting extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (UserDAO userDAO = new UserDAO()) {
            if (userDAO.getUserCount() < 2) {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                return;
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        // check all parameters are present and not duplicates
        // and check that all parameters are castable to their respective subtypes
        if (HTTPParameterChecker.checkExistsUnique(request, "title") &&
                HTTPParameterChecker.checkExistsUnique(request, "start") &&
                HTTPParameterChecker.checkExistsUnique(request, "duration") &&
                HTTPParameterChecker.checkExistsUnique(request, "max_participants") &&
                HTTPParameterChecker.checkExists(request, "selected") &&
                HTTPParameterChecker.checkType(request, "start", ZonedDateTime::parse) &&
                HTTPParameterChecker.checkType(request, "duration", Integer::parseInt) &&
                HTTPParameterChecker.checkType(request, "max_participants", Integer::parseInt) &&
                HTTPParameterChecker.checkType(request, "selected", Long::parseLong)) {
            // extract the parameters into the respective subtypes
            String title = request.getParameter("title").trim();
            ZonedDateTime start = ZonedDateTime.parse(request.getParameter("start"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            int maxParticipants = Integer.parseInt(request.getParameter("max_participants"));
            Set<Long> selected = Arrays.stream(request.getParameterValues("selected"))
                    .map(Long::parseLong)
                    .collect(Collectors.toUnmodifiableSet());

            UserBean userBean = (UserBean) request.getSession().getAttribute(Constants.USER);

            if (!title.isEmpty() && title.getBytes(StandardCharsets.UTF_8).length <= 50 &&
                    start.isAfter(ZonedDateTime.now()) &&
                    duration > 0 &&
                    maxParticipants > 0 &&
                    selected.size() <= maxParticipants &&
                    !selected.contains(userBean.getId())) {
                try (MeetingDAO meetingDAO = new MeetingDAO()) {
                    MeetingBean meetingBean = new MeetingBean();
                    meetingBean.setTitle(title);
                    meetingBean.setStart(start.with(ZoneOffset.UTC).toInstant());
                    meetingBean.setDuration(duration);
                    meetingBean.setCapacity(maxParticipants);
                    meetingBean.setAdmin(userBean);
                    meetingBean.setInvites(selected);

                    meetingDAO.createMeeting(meetingBean);

                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (SQLException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
