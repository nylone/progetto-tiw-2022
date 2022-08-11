package com.rampeo.tiw.progetto2022.Filters;

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

@WebFilter(filterName = "UnAuthFilter", urlPatterns = {URLs.AUTH_PAGE, URLs.LOGIN, URLs.SIGNUP})
public class UnAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();

        if (session.getAttribute(AttributeNames.USER) != null) {
            hresponse.sendRedirect(new PathBuilder(URLs.ERROR_PAGE)
                    .addParam(AttributeNames.ERROR, ErrorParameter.AUTH_ALREADY_LOGGED_IN)
                    .addParam(AttributeNames.REDIRECT, URLs.HOME_PAGE)
                    .toString());
            return;
        }

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }
}
