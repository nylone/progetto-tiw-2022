package com.rampeo.tiw.progetto2022.Servlets.Views;

import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Constants.URLs;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DefaultPage", value = "/")
public class DefaultPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute(AttributeNames.USER) != null) {
            response.sendRedirect(new PathBuilder(URLs.HOME_PAGE)
                            .addParam(AttributeNames.ERROR, ErrorParameter.UNREACHABLE)
                    .toString());
        } else {
            response.sendRedirect(new PathBuilder(URLs.AUTH_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.AUTH_NOT_LOGGED_IN)
                    .toString());
        }
    }
}
