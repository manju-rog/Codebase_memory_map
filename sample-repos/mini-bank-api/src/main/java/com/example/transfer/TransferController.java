package com.example.transfer;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    
    private final TransferService transferService;
    
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }
    
    @PostMapping
    public Transaction createTransfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}
