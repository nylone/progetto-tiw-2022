package com.rampeo.tiw.progetto2022.Filters;

import com.rampeo.tiw.progetto2022.Utils.Constants;
import com.rampeo.tiw.progetto2022.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UnAuthFilter", urlPatterns = {
        Constants.AUTH_PAGE,
        Constants.LOGIN_ENDPOINT,
        Constants.REGISTER_ENDPOINT,
})
public class UnAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();

        if (session.getAttribute(Constants.USER) != null) {
            hresponse.sendRedirect(new PathBuilder(Constants.HOME_PAGE)
                    .toString());
            return;
        }

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }
}
