package com.dev.financemanager.service.finance;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;

import java.util.List;
import java.util.Optional;

public interface FinanceService {
    List<Finance> findAll();

    Optional<Finance> findById(Long id);

    Finance save(Finance finance);

    void delete(Finance finance);

    List<Finance> findAllByUser(AppUser user);
}
