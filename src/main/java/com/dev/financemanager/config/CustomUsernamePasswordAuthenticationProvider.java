package com.dev.financemanager.config;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.service.users.AppUsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AppUsersService usersService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        AppUser user = usersService.findByEmail(username);
        if (user != null) {
            if (passwordEncoder.matches(pwd, user.getPassword())) {
                List<GrantedAuthority> authorities =
                        user.getAuthorities()
                                .stream()
                                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                                .collect(Collectors.toList());

                Authentication auth = new UsernamePasswordAuthenticationToken(username, pwd, authorities);

                // Save the authentication object in SecurityContext
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(auth);
                SecurityContextHolder.setContext(context);

                return auth;
            } else {
                throw new BadCredentialsException("Invalid password!");
            }
        } else {
            throw new BadCredentialsException("No user registered with these details!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
