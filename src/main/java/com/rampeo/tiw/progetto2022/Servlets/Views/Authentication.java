package com.rampeo.tiw.progetto2022.Servlets.Views;

import com.rampeo.tiw.progetto2022.Constants.URLs.ViewsURLs;
import com.rampeo.tiw.progetto2022.Servlets.ThymeleafHTTPServlet;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Authentication", urlPatterns = {ViewsURLs.AUTH_PAGE})
public class Authentication extends ThymeleafHTTPServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String template = "auth";
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        getTemplateEngine().process(template, ctx, response.getWriter());
    }
}
