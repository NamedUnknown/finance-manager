package com.dev.financemanager.service.finance;

import com.dev.financemanager.dao.FinanceRepository;
import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository financeRepository;

    @Override
    public List<Finance> findAll() {
        return financeRepository.findAll();
    }

    @Override
    public Finance save(Finance finance) {
         return financeRepository.save(finance);
    }

    @Override
    public void delete(Finance finance) {
        financeRepository.delete(finance);
    }

    @Override
    public List<Finance> findAllByUser(AppUser user) {
        return  financeRepository.findAllByUser(user);
    }
}
