package com.dev.financemanager.filter;

import com.dev.financemanager.dto.request.AuthenticationRequest;
import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.service.users.AppUsersService;
import com.dev.financemanager.utils.Base64TokenUtils;
import com.dev.financemanager.utils.JwtTokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/auth/login", "POST");

    private final AppUsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final Base64TokenUtils base64TokenUtils;


    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager, AppUsersService usersService, PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils, Base64TokenUtils base64TokenUtils) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
        this.base64TokenUtils = base64TokenUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String headerValue = request.getHeader("Authorization");
        if (headerValue == null) {
            return null;
        }
        AuthenticationRequest credentials = base64TokenUtils.fetchCredentials(headerValue);
        if (credentials != null) {
            AppUser user = usersService.findByEmail(credentials.getEmail());
            if (user == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "No user with email: " + credentials.getEmail() + " registered!");
                return null;
            }
            if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
                List<GrantedAuthority> authorities =
                        user.getAuthorities()
                                .stream()
                                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                                .collect(Collectors.toList());

                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), credentials.getPassword(), authorities);

                String jwtToken = jwtTokenUtils.generateToken(auth);

                response.setHeader("Authorization", "Bearer " + jwtToken);

                // Save the authentication object in SecurityContext
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(auth);
                SecurityContextHolder.setContext(context);
            }
        }
        return null;
    }
}
