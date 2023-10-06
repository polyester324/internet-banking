package com.tms.service;

import com.tms.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean transferMoneyBetweenTwoClients(String cardSender, String cardReceiver, BigDecimal amount){
        return transactionRepository.transferMoneyBetweenTwoClients(cardSender, cardReceiver, amount);
    }

    public boolean putMoneyIntoTheAccount(String card, BigDecimal amount, String moneyCurrency){
        return transactionRepository.putMoneyIntoTheAccount(card, amount, moneyCurrency);
    }

    public boolean withdrawMoneyFromTheAccount(String card, BigDecimal amount, String moneyCurrency){
        return transactionRepository.withdrawMoneyFromTheAccount(card, amount, moneyCurrency);
    }


}
