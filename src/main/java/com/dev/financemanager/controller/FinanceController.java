package com.dev.financemanager.controller;

import com.dev.financemanager.dto.response.ErrorResponse;
import com.dev.financemanager.dto.response.SuccessfulResponse;
import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Finance;
import com.dev.financemanager.service.finance.FinanceService;
import com.dev.financemanager.service.users.AppUsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/finances")
public class FinanceController {

    private final FinanceService financeService;

    private final AppUsersService usersService;

    @GetMapping
    public List<Finance> getAllByEmail() {
        AppUser user = getUserFromSecurityContext();
        List<Finance> finances = financeService.findAllByUser(user);
        if (finances == null || finances.isEmpty()) return new ArrayList<>();
        return finances;
    }

    @PutMapping
    public ResponseEntity<Object> updateFinance(@RequestBody Finance finance) {
        AppUser user = getUserFromSecurityContext();
        if (user == null) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User could not be found"),
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            finance.setUser(user);
            finance = financeService.save(finance);
            if (finance == null) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Record could not be saved"),
                        HttpStatus.BAD_REQUEST
                );
            }
            return new ResponseEntity<>(
                    new SuccessfulResponse<>(HttpStatus.OK.value(), "Record updated", finance),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Record could not be saved." + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteFinance(@RequestBody Finance finance) {
        try {
            finance.setUser(null);
            financeService.delete(finance);
            return new ResponseEntity<>(
                    new SuccessfulResponse<>(HttpStatus.OK.value(), "Record deleted", null),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Record could not be deleted." + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping
    public ResponseEntity<Object> addFinance(@RequestBody Finance finance) {
        try {
            AppUser user = getUserFromSecurityContext();
            finance.setId(0L);
            finance.setUser(user);
            return new ResponseEntity<>(
                    new SuccessfulResponse<>(HttpStatus.OK.value(), "Record deleted", null),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Record could not be deleted." + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private AppUser getUserFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            throw new RuntimeException("No user authenticated");
        }
        return usersService.findByEmail(email);
    }
}
