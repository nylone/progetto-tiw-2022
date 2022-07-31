package com.rampeo.tiw.progetto2022.Servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class ThymeleafHTTPServlet extends HttpServlet {
    private TemplateEngine templateEngine = null;

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode("XHTML");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("/WEB-INF/templates/");
    }
}
