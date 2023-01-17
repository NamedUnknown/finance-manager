package com.dev.financemanager.service.savings;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;
import com.dev.financemanager.entity.Savings;

import java.util.List;

public interface SavingsService {
    List<Savings> findAll();

    Savings save(Savings finance);

    void delete(Savings finance);

    List<Savings> findAllByUser(AppUser user);
}
