package com.tms.controller;

import com.tms.dtos.CardTransactionDepositAndWithdrawDTO;
import com.tms.dtos.CardTransactionTransferDTO;
import com.tms.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operation")
public class OperationController {
    public final TransactionService transactionService;

    public OperationController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/transfer")
    public ResponseEntity<HttpStatus> transferMoneyBetweenTwoClients(@RequestBody CardTransactionTransferDTO dto){
        return new ResponseEntity<>(transactionService.transferMoneyBetweenTwoClients(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/deposit")
    public ResponseEntity<HttpStatus> putMoneyIntoTheAccount(@RequestBody CardTransactionDepositAndWithdrawDTO dto){
        return new ResponseEntity<>(transactionService.putMoneyIntoTheAccount(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdrawMoneyFromTheAccount(@RequestBody CardTransactionDepositAndWithdrawDTO dto){
        return new ResponseEntity<>(transactionService.withdrawMoneyFromTheAccount(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
