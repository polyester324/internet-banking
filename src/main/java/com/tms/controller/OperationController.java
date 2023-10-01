package com.tms.controller;

import com.tms.domain.Card;
import com.tms.dtos.CardTransactionDTO;
import com.tms.service.CardService;
import com.tms.service.TransactionService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@RestController
@RequestMapping("/operation")
public class OperationController {
    public final TransactionService transactionService;

    public OperationController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/transfer")
    public Boolean transferMoneyBetweenTwoClients(@RequestBody CardTransactionDTO dto){
        return transactionService.transferMoneyBetweenTwoClients(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount());
    }
}
