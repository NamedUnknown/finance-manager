package com.dev.financemanager.controller;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Savings;
import com.dev.financemanager.service.savings.SavingsService;
import com.dev.financemanager.service.users.AppUsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/savings")
@AllArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;
    private final AppUsersService usersService;

    @GetMapping
    public List<Savings> getAll() {
        AppUser user = getUserFromSecurityContext();
        List<Savings> savings = savingsService.findAllByUser(user);
        if (savings == null || savings.isEmpty()) return new ArrayList<>();
        return savings;
    }

    private AppUser getUserFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            throw new RuntimeException("No user authenticated");
        }
        return usersService.findByEmail(email);
    }

}
