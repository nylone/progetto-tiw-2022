package com.rampeo.tiw.progetto2022.Servlets.Filters;

import com.rampeo.tiw.progetto2022.Constants.Attributes.AttributeNames;
import com.rampeo.tiw.progetto2022.Constants.Attributes.ErrorParameter;
import com.rampeo.tiw.progetto2022.Constants.URLs;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {URLs.AUTHENTICATED_PATTERN})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();

        if (session.isNew() || session.getAttribute(AttributeNames.USER) == null) {
            hresponse.sendRedirect(new PathBuilder(URLs.AUTH_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.AUTH_NOT_LOGGED_IN)
                    .toString());
            return;
        }

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }
}
