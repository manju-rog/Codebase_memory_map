package com.example.account;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
