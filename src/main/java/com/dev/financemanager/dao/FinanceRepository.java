package com.dev.financemanager.dao;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {
    List<Finance> findAllByUser(AppUser user);
}
