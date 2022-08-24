package com.rampeo.tiw.progetto2022.Controllers;

import com.rampeo.tiw.progetto2022.Beans.UserBean;
import com.rampeo.tiw.progetto2022.DAOs.UserDAO;
import com.rampeo.tiw.progetto2022.Utils.Constants;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(name = "Logout", urlPatterns = {Constants.LOGOUT_ENDPOINT})
public class DoLogout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute(Constants.USER);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
