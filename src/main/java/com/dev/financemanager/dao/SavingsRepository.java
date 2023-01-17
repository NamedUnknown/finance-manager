package com.dev.financemanager.dao;

import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;
import com.dev.financemanager.entity.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {

    List<Savings> findAllByUser(AppUser user);
}
