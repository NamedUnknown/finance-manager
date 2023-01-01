package com.dev.financemanager.filter;

import com.dev.financemanager.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Boolean jwtExists = checkIfJwtExists(request);
            SecurityContext context = SecurityContextHolder.getContext();
            if (jwtExists) {
                String jwtToken = jwtTokenUtils.extractToken(request);

                Claims claims = jwtTokenUtils.validateToken(jwtToken);
                if (claims != null) {
                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    claims.get("email"),
                                    null,
                                    authorities
                                            .stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toList()));
                    context.setAuthentication(auth);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private Boolean checkIfJwtExists(HttpServletRequest request) {
        String authenticationHeader = request.getHeader("Authorization");
        return authenticationHeader != null && authenticationHeader.startsWith("Bearer ");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/auth/login");
    }
}
