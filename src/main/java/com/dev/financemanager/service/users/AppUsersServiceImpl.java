package com.dev.financemanager.service.users;

import com.dev.financemanager.dao.AppUsersRepository;
import com.dev.financemanager.entity.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUsersServiceImpl implements AppUsersService{

    private final AppUsersRepository usersRepository;

    public AppUsersServiceImpl(AppUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<AppUser> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public AppUser findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public AppUser save(AppUser user) {
        return usersRepository.save(user);
    }

    @Override
    public void delete(AppUser user) {
        usersRepository.delete(user);
    }
}
