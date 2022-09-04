package com.rampeo.tiw.progetto2022.Servlets.Views;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Constants.URLs;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Invites", urlPatterns = {URLs.LINK_TO_MEETING_PAGE})
public class GetInvites extends ThymeleafHTTPServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // make into a filter
        if (request.getSession().getAttribute(AttributeNames.MEETING_CREATION) == null) {
            response.sendRedirect(new PathBuilder(URLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.CREATION_NO_MEETING_PENDING)
                    .addParam(AttributeNames.REDIRECT, URLs.HOME_PAGE)
                    .toString());
            return;
        }
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        final UserBean user = (UserBean) request.getSession().getAttribute(AttributeNames.USER);
        try (UserDAO userDAO = new UserDAO()) {
            ctx.setVariable(AttributeNames.INVITED_USERS, userDAO.getOtherUsers(user));
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.UNKNOWN)
                    .addParam(AttributeNames.REDIRECT, URLs.HOME_PAGE)
                    .toString());
            return;
        }
        final String template = "user_data";
        getTemplateEngine().process(template, ctx, response.getWriter());
    }
}
