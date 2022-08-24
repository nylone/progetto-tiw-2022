package com.rampeo.tiw.progetto2022.Controllers;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.Utils.Constants;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(name = "Login", urlPatterns = {Constants.LOGIN_ENDPOINT})
@MultipartConfig
public class DoLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usrn = request.getParameter("email");
        String pwd = request.getParameter("pass");

        if (usrn == null || usrn.isEmpty() || pwd == null || pwd.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserBean u;
        try (UserDAO userDAO = new UserDAO()) {
            u = userDAO.authenticate(usrn, pwd);
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if (u == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            request.getSession().setAttribute(Constants.USER, u);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
