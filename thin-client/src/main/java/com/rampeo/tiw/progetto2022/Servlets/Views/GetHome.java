package com.rampeo.tiw.progetto2022.Servlets.Views;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Constants.URLs;
import com.rampeo.tiw.progetto2022.DAOs.MeetingDAO;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Home", urlPatterns = {URLs.HOME_PAGE})
public class GetHome extends ThymeleafHTTPServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final UserBean user = (UserBean) request.getSession().getAttribute(AttributeNames.USER);
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        try (MeetingDAO meetingDAO = new MeetingDAO()) {
            ctx.setVariable("createdMeetings", meetingDAO.getCreatedMeetings(user));
            ctx.setVariable("invitedMeetings", meetingDAO.getInvitedMeetings(user));
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.UNKNOWN)
                    .addParam(AttributeNames.REDIRECT, URLs.HOME_PAGE)
                    .toString());
            return;
        }
        final String template = "home";
        getTemplateEngine().process(template, ctx, response.getWriter());
    }
}
