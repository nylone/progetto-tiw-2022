package com.rampeo.tiw.progetto2022.Servlets.Controllers;

import com.rampeo.tiw.progetto2022.Beans.MeetingCreationBean;
import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.URLs.ControllersURLs;
import com.rampeo.tiw.progetto2022.Constants.URLs.ViewsURLs;
import com.rampeo.tiw.progetto2022.DAOs.MeetingDAO;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import com.rampeo.tiw.progetto2022.Utils.HTTPParameterChecker;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "CompleteMeeting", urlPatterns = {ControllersURLs.COMPLETE_MEETING})
public class CompleteMeeting extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        if (request.getSession().getAttribute(AttributeNames.MEETING_CREATION) == null) {
            response.sendRedirect(new PathBuilder(ViewsURLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_NO_MEETING_PENDING)
                    .addParam(AttributeNames.REDIRECT, ViewsURLs.HOME_PAGE)
                    .toString());
            return;
        }

        // check all parameters are present and not duplicates
        // and check that all parameters are castable to their respective subtypes
        if (HTTPParameterChecker.checkExists(request, "invite") &&
                HTTPParameterChecker.checkType(request, "invite", Long::parseLong)
        ) {
            Set<Long> invited = Arrays.stream(request.getParameterValues("invite"))
                    .map(Long::parseLong)
                    .collect(Collectors.toUnmodifiableSet());
            MeetingCreationBean creationBean = (MeetingCreationBean) session.getAttribute(AttributeNames.MEETING_CREATION);
            creationBean.setSelected(invited);

            if (invited.size() > creationBean.getMeetingBean().getMaxParticipants()) {
                creationBean.setFailedAttempts(creationBean.getFailedAttempts() + 1);
                if (creationBean.getFailedAttempts() < 3) {
                    response.sendRedirect(new PathBuilder(ViewsURLs.LINK_TO_MEETING_PAGE)
                            .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_MAX_PARTICIPANTS)
                            .toString());
                } else {
                    session.removeAttribute(AttributeNames.MEETING_CREATION);
                    response.sendRedirect(new PathBuilder(ViewsURLs.ERROR_PAGE)
                            .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_MAX_ATTEMPTS)
                            .addParam(AttributeNames.REDIRECT, ViewsURLs.HOME_PAGE)
                            .toString());
                }
                return;
            }

            if (invited.contains(creationBean.getMeetingBean().getAdmin().getId())) {
                response.sendRedirect(new PathBuilder(ViewsURLs.ERROR_PAGE)
                        .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_ADMIN_INVITED)
                        .addParam(AttributeNames.REDIRECT, ViewsURLs.LINK_TO_MEETING_PAGE)
                        .toString());
                return;
            }

            try (MeetingDAO meetingDAO = new MeetingDAO()) {
                meetingDAO.createMeeting(creationBean.getMeetingBean(), invited.stream().toList());
            } catch (SQLException e) {
                // todo send to errors page
                session.removeAttribute(AttributeNames.MEETING_CREATION);
                response.sendRedirect(new PathBuilder(ViewsURLs.ERROR_PAGE)
                        .addParam(AttributeNames.ERROR, ErrorParameter.UNKNOWN)
                        .addParam(AttributeNames.REDIRECT, ViewsURLs.HOME_PAGE)
                        .toString());
                return;
            }
            session.removeAttribute(AttributeNames.MEETING_CREATION);
            response.sendRedirect(ViewsURLs.HOME_PAGE);
        } else {
            response.sendRedirect(new PathBuilder(ViewsURLs.LINK_TO_MEETING_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_NO_PARTICIPANTS)
                    .toString());
        }
    }


}
