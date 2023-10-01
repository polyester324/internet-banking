package com.tms.service;

import com.tms.repository.TransactionRepository;
import java.math.BigDecimal;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean transferMoneyBetweenTwoClients(String cardSender, String cardReceiver, BigDecimal amount){
        return transactionRepository.transferMoneyBetweenTwoClients(cardSender, cardReceiver, amount);
    }
}
