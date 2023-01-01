package com.dev.financemanager.service.savings;

import com.dev.financemanager.dao.SavingsRepository;
import com.dev.financemanager.entity.Savings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsServiceImpl implements SavingsService {

    private final SavingsRepository savingsRepository;

    public SavingsServiceImpl(SavingsRepository savingsRepository) {
        this.savingsRepository = savingsRepository;
    }

    @Override
    public List<Savings> findAll() {
        return savingsRepository.findAll();
    }

    @Override
    public Savings save(Savings savings) {
        return savingsRepository.save(savings);
    }

    @Override
    public void delete(Savings savings) {
        savingsRepository.delete(savings);
    }
}
