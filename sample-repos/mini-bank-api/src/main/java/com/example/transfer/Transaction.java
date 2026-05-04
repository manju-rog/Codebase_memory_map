package com.example.transfer;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private BigDecimal amount;
    private Long fromAccountId;
    private Long toAccountId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Long getFromAccountId() {
        return fromAccountId;
    }
    
    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
    
    public Long getToAccountId() {
        return toAccountId;
    }
    
    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }
}
