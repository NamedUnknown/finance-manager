package com.dev.financemanager.controller;

import com.dev.financemanager.dto.response.ErrorResponse;
import com.dev.financemanager.dto.response.SuccessfulResponse;
import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.service.users.AppUsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AppUsersService usersService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public AppUser viewProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.findByEmail(email);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody AppUser user) {
        AppUser savedUser = null;
        ResponseEntity<Object> response = null;

        // Check if the user with this email exists
        AppUser checkUser = usersService.findByEmail(user.getEmail());
        if (checkUser != null) {
            response = new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User with email address already exists!"), HttpStatus.BAD_REQUEST);
        } else {
            try {
                String hashPwd = passwordEncoder.encode(user.getPassword());
                user.setId(0L);
                user.setPassword(hashPwd);
                user.setCreated(new Date(System.currentTimeMillis()));
                savedUser = usersService.save(user);
                if (savedUser.getId() > 0) {
                    response = new ResponseEntity<>(new SuccessfulResponse<Object>(HttpStatus.CREATED.value(), "User details are successfully registered", null), HttpStatus.CREATED);
                }
            } catch (Exception ex) {
                response = new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An exception occurred due to " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return response;
    }
}
