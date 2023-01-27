package com.dev.financemanager.config;

import com.dev.financemanager.filter.*;
import com.dev.financemanager.service.users.AppUsersService;
import com.dev.financemanager.utils.Base64TokenUtils;
import com.dev.financemanager.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final Base64TokenUtils base64TokenUtils;
    private final AppUsersService usersService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        EmailPasswordAuthenticationFilter emailPasswordFilter = emailPasswordAuthenticationFilter(http);

        // https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework
        //CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        //XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        // set the name of the attribute the CsrfToken will be populated on
        //delegate.setCsrfRequestAttributeName("_csrf");
        // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
        // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandler
        //CsrfTokenRequestHandler requestHandler = delegate::handle;

        // Session has to be saved in some way
        // Database table called [sessions] -> Session ID, Session token
        // Store tokens in: cookies or local storage
        return http
                .securityContext().requireExplicitSave(false)
                .and()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/auth/register", "/auth/login").permitAll()
                                .requestMatchers("/auth").hasRole("USER")
                                .requestMatchers("/finances", "/savings", "/contact/**").hasRole("USER")
                .and()
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.setAllowedMethods(List.of(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.DELETE.name(),
                            // For preflight requests
                            HttpMethod.OPTIONS.name()
                    ));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of(
                            HttpHeaders.AUTHORIZATION,
                            HttpHeaders.ACCEPT,
                            HttpHeaders.CONTENT_TYPE
                    ));
                    config.setMaxAge(3600L);
                    return config;
                })
                .and()
                .csrf().disable()
                .addFilterBefore(emailPasswordFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter(HttpSecurity http) throws Exception {
        return new EmailPasswordAuthenticationFilter(authManager(http),  usersService, passwordEncoder, jwtTokenUtils, base64TokenUtils);
    }

    @Bean
    public JwtValidationFilter jwtValidationFilter() {
        return new JwtValidationFilter(jwtTokenUtils);
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new CustomUsernamePasswordAuthenticationProvider(usersService, passwordEncoder));
        return authenticationManagerBuilder.build();
    }
}
