package com.example.account;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }
}
