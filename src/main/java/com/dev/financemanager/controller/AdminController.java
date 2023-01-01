package com.dev.financemanager.controller;

import com.dev.financemanager.entity.Finance;
import com.dev.financemanager.entity.Savings;
import com.dev.financemanager.service.finance.FinanceService;
import com.dev.financemanager.service.savings.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final SavingsService savingsService;

    private final FinanceService financeService;

    @Autowired
    public AdminController(SavingsService savingsService, FinanceService financeService) {
        this.savingsService = savingsService;
        this.financeService = financeService;
    }


    @GetMapping("/finances")
    public List<Finance> fetchAllFinances() {
        return financeService.findAll();
    }

    @GetMapping("/savings")
    public List<Savings> fetchAllSavings() {
        return savingsService.findAll();
    }
}
