package com.info6250.bts.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class XSSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {

    }
}
