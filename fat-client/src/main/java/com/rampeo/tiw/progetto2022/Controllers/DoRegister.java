package com.rampeo.tiw.progetto2022.Controllers;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;
import com.rampeo.tiw.progetto2022.Utils.Constants;
import com.rampeo.tiw.progetto2022.Utils.InputStringChecker;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(name = "Register", urlPatterns = {Constants.REGISTER_ENDPOINT})
@MultipartConfig
public class DoRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String email = request.getParameter("email");
        final String pass = request.getParameter("pass");

        if (email == null || email.isEmpty() || pass == null || pass.isEmpty() ||
                !InputStringChecker.checkEmail(email) ||
                !InputStringChecker.checkPass(pass)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (UserDAO userDAO = new UserDAO()) {
            UserBean u;
            if (userDAO.checkUnique(email)) {
                userDAO.addCredentials(email, pass);
                u = userDAO.authenticate(email, pass);
                request.getSession().setAttribute(Constants.USER, u);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
