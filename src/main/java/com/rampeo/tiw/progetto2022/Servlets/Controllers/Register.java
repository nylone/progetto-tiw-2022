package com.rampeo.tiw.progetto2022.Servlets.Controllers;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.URLs.ControllersURLs;
import com.rampeo.tiw.progetto2022.Constants.URLs.ViewsURLs;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import com.rampeo.tiw.progetto2022.Utils.InputStringChecker;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(name = "Register", urlPatterns = {ControllersURLs.SIGNUP})
public class Register extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String email = request.getParameter("email");
        final String pass = request.getParameter("pass1");
        final String r_pass = request.getParameter("pass2");

        if (email == null || email.isEmpty() || pass == null || pass.isEmpty() || r_pass == null || r_pass.isEmpty()) {
            response.sendRedirect(new PathBuilder(ViewsURLs.AUTH_PAGE).addParam(AttributeNames.ERROR, ErrorParameter.AUTH_EMPTY_FIELDS).toString());
            return;
        }
        if (!InputStringChecker.checkEmail(email)) {
            response.sendRedirect(new PathBuilder(ViewsURLs.AUTH_PAGE).addParam(AttributeNames.ERROR, ErrorParameter.AUTH_INVALID_EMAIL).toString());
            return;
        }
        if (!pass.equals(r_pass) || !InputStringChecker.checkPass(pass)) {
            response.sendRedirect(new PathBuilder(ViewsURLs.AUTH_PAGE).addParam(AttributeNames.ERROR, ErrorParameter.AUTH_INVALID_PASSWORD).toString());
            return;
        }

        try (UserDAO userDAO = new UserDAO()) {
            UserBean u;
            if (userDAO.checkUnique(email)) {
                userDAO.addCredentials(email, pass);
                u = userDAO.checkCredentials(email, pass);
                request.getSession().setAttribute(AttributeNames.USER, u);
                response.sendRedirect(ViewsURLs.HOME_PAGE);
            } else {
                response.sendRedirect(new PathBuilder(ViewsURLs.AUTH_PAGE).addParam(AttributeNames.ERROR, ErrorParameter.AUTH_USER_EXISTS).toString());
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.sendRedirect(new PathBuilder(ViewsURLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.UNKNOWN)
                    .addParam(AttributeNames.REDIRECT, ViewsURLs.AUTH_PAGE)
                    .toString());
        }

    }
}
