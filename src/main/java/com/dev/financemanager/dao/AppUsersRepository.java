package com.dev.financemanager.dao;

import com.dev.financemanager.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUsersRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);
}
