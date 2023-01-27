package com.dev.financemanager.controller;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Savings;
import com.dev.financemanager.service.savings.SavingsService;
import com.dev.financemanager.utils.AuthUtils;
import lombok.AllArgsConstructor;
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
    private final AuthUtils authUtils;

    @GetMapping
    public List<Savings> getAll() {
        AppUser user = authUtils.getUserFromSecurityContext();
        List<Savings> savings = savingsService.findAllByUser(user);
        if (savings == null || savings.isEmpty()) return new ArrayList<>();
        return savings;
    }
}
