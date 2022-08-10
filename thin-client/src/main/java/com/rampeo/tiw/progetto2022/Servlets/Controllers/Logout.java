package com.rampeo.tiw.progetto2022.Servlets.Controllers;

import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.URLs;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Logout", urlPatterns = {URLs.LOGOUT})
public class Logout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect(new PathBuilder(URLs.AUTH_PAGE)
                .addParam(AttributeNames.SUCCESS, "logout")
                .toString());
    }
}
