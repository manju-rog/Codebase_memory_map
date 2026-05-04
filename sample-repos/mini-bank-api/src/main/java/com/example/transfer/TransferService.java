package com.example.transfer;

import com.example.account.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    public Transaction transfer(TransferRequest request) {
        // Perform transfer logic
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        return transactionRepository.save(transaction);
    }
}
