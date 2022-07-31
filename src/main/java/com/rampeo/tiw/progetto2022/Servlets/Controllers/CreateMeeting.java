package com.rampeo.tiw.progetto2022.Servlets.Controllers;

import com.rampeo.tiw.progetto2022.Beans.MeetingBean;
import com.rampeo.tiw.progetto2022.Beans.MeetingCreationBean;
import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.URLs.ControllersURLs;
import com.rampeo.tiw.progetto2022.Constants.URLs.ViewsURLs;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import com.rampeo.tiw.progetto2022.Utils.HTTPParameterChecker;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Set;

@WebServlet(name = "CreateMeeting", urlPatterns = {ControllersURLs.CREATE_MEETING})
public class CreateMeeting extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // check all parameters are present and not duplicates
        // and check that all parameters are castable to their respective subtypes
        if (HTTPParameterChecker.checkExistsUnique(request, "title") &&
                HTTPParameterChecker.checkExistsUnique(request, "start") &&
                HTTPParameterChecker.checkExistsUnique(request, "duration") &&
                HTTPParameterChecker.checkExistsUnique(request, "max_participants") &&
                HTTPParameterChecker.checkType(request, "start", LocalDateTime::parse) &&
                HTTPParameterChecker.checkType(request, "duration", Integer::parseInt) &&
                HTTPParameterChecker.checkType(request, "max_participants", Integer::parseInt)
        ) {
            // extract the parameters into the respective subtypes
            String title = request.getParameter("title").trim();
            LocalDateTime start = LocalDateTime.parse(request.getParameter("start"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            int maxParticipants = Integer.parseInt(request.getParameter("max_participants"));

            if (!title.isEmpty() && title.getBytes(StandardCharsets.UTF_8).length <= 50 &&
                    start.isAfter(LocalDateTime.now()) &&
                    duration > 0 &&
                    maxParticipants > 0
            ) {
                Date date = Date.valueOf(start.toLocalDate());
                Time time = Time.valueOf(start.toLocalTime());

                MeetingBean meetingBean = new MeetingBean();
                meetingBean.setTitle(title);
                meetingBean.setDate(date);
                meetingBean.setTime(time);
                meetingBean.setDuration(duration);
                meetingBean.setMaxParticipants(maxParticipants);
                meetingBean.setAdmin(((UserBean) request.getSession().getAttribute(AttributeNames.USER)));

                MeetingCreationBean meetingCreationBean = new MeetingCreationBean();
                meetingCreationBean.setMeetingBean(meetingBean);
                meetingCreationBean.setSelected(Set.of());
                meetingCreationBean.setFailedAttempts(0);

                request.getSession().setAttribute(AttributeNames.MEETING_CREATION, meetingCreationBean);
                response.sendRedirect(ViewsURLs.LINK_TO_MEETING_PAGE);
                return;
            }
        }

        response.sendRedirect(new PathBuilder(ViewsURLs.HOME_PAGE)
                .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_INVALID_PARAMETERS)
                .toString());
    }


}
