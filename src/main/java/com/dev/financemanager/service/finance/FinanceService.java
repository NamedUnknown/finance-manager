package com.dev.financemanager.service.finance;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;

import java.util.List;

public interface FinanceService {
    List<Finance> findAll();

    Finance save(Finance finance);

    void delete(Finance finance);

    List<Finance> findAllByUser(AppUser user);
}
