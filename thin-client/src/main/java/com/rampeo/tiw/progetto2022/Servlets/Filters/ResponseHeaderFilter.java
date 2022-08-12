package com.rampeo.tiw.progetto2022.Servlets.Filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "ResponseHeaderFilter", urlPatterns = "/*")
public class ResponseHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // This prevents triggering engine executions for resource URLs
        HttpServletRequest req = (HttpServletRequest) request;
        if (!req.getRequestURI().startsWith("/css") &&
                //!req.getRequestURI().startsWith("/images") &&
                !req.getRequestURI().startsWith("/favicon")) {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setContentType("text/html;charset=UTF-8");
            res.setHeader("Pragma", "no-cache");
            res.setHeader("Cache-Control", "no-cache");
        }
        chain.doFilter(request, response);
    }
}
