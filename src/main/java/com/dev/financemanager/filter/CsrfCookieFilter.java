package com.dev.financemanager.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final String CSRF_COOKIE_NAME = "XSRF_TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if(csrfToken.getHeaderName() != null) {
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies != null && requestCookies.length > 0) {
            List<Cookie> filteredCookies = Arrays.stream(requestCookies).filter(c -> c.getName().equals(CSRF_COOKIE_NAME)).toList();
            if (!filteredCookies.isEmpty()) {
                Cookie cookie = filteredCookies.get(0);
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request, response);
    }

}
