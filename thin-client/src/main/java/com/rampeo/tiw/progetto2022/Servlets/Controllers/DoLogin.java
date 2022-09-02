package com.rampeo.tiw.progetto2022.Servlets.Controllers;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Constants.URLs;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(name = "Login", urlPatterns = {URLs.LOGIN})
public class DoLogin extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usrn = request.getParameter("email");
        String pwd = request.getParameter("pass");

        if (usrn == null || usrn.isEmpty() || pwd == null || pwd.isEmpty()) {
            response.sendRedirect(new PathBuilder(URLs.AUTH_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.AUTH_EMPTY_FIELDS)
                    .toString());
            return;
        }

        UserBean u;
        try (UserDAO userDAO = new UserDAO()) {
            u = userDAO.checkCredentials(usrn, pwd);
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.sendRedirect(new PathBuilder(URLs.AUTH_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.UNKNOWN)
                    .toString());
            return;
        }

        if (u == null) {
            response.sendRedirect(new PathBuilder(URLs.AUTH_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.AUTH_FAILED_AUTHENTICATION)
                    .toString());
        } else {
            request.getSession().setAttribute(AttributeNames.USER, u);
            response.sendRedirect(URLs.HOME_PAGE);
        }
    }
}
