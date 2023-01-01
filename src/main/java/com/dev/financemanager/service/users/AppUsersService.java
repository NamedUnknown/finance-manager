package com.dev.financemanager.service.users;

import com.dev.financemanager.entity.AppUser;

import java.util.List;

public interface AppUsersService {
    List<AppUser> findAll();

    AppUser findByEmail(String email);

    AppUser save(AppUser user);

    void delete(AppUser user);
}
