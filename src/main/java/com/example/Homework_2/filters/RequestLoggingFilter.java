package com.example.Homework_2.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Enumeration;

@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

    private ServletContext context;

    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("RequestLoggingFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        logRequestParams(req);
        logCookies(req);

        chain.doFilter(request, response);
    }

    private void logRequestParams(HttpServletRequest req) {
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = req.getParameter(name);
            this.context.log(req.getRemoteAddr() + "::Request Params::{" + name + "=" + value + "}");
        }
    }

    private void logCookies(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                this.context.log(req.getRemoteAddr() + "::Cookie::{" + cookie.getName() + "," + cookie.getValue() + "}");
            }
        }
    }

    public void destroy() {
    }
}