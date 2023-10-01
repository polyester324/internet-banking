package com.tms.controller;

import com.tms.domain.Card;
import com.tms.dtos.CardTransactionDTO;
import com.tms.service.CardService;
import com.tms.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HttpStatus> transferMoneyBetweenTwoClients(@RequestBody CardTransactionDTO dto){
        return new ResponseEntity<>(transactionService.transferMoneyBetweenTwoClients(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
