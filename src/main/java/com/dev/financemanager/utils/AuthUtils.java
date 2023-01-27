package com.dev.financemanager.utils;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.service.users.AppUsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthUtils {

    private final AppUsersService usersService;

    public AppUser getUserFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            throw new RuntimeException("No user authenticated");
        }
        return usersService.findByEmail(email);
    }
}
