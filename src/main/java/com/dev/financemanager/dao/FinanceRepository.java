package com.dev.financemanager.dao;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {
    List<Finance> findAllByUser(AppUser user);

    Optional<Finance> findById(Long id);
}
