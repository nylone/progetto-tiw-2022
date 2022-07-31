package com.rampeo.tiw.progetto2022.Servlets.Controllers;

import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.URLs.ControllersURLs;
import com.rampeo.tiw.progetto2022.Constants.URLs.ViewsURLs;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Logout", urlPatterns = {ControllersURLs.LOGOUT})
public class Logout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (!session.isNew() && session.getAttribute(AttributeNames.USER) != null) {
            session.invalidate();
            response.sendRedirect(new PathBuilder(ViewsURLs.AUTH_PAGE)
                    .addParam(AttributeNames.SUCCESS, "logout")
                    .toString());
        } else {
            response.sendRedirect(new PathBuilder(ViewsURLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.AUTH_NOT_LOGGED_IN)
                    .addParam(AttributeNames.REDIRECT, ViewsURLs.HOME_PAGE)
                    .toString());
        }
    }
}
