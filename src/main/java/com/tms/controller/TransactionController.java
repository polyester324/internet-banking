package com.tms.controller;

import com.tms.dtos.CardTransactionDepositAndWithdrawDTO;
import com.tms.dtos.CardTransactionTransferDTO;
import com.tms.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    public final CardService cardService;

    public TransactionController(CardService cardService) {
        this.cardService = cardService;
    }


    @PutMapping("/transfer")
    public ResponseEntity<HttpStatus> transferMoneyBetweenTwoClients(@RequestBody CardTransactionTransferDTO dto){
        return new ResponseEntity<>(cardService.transfer(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/deposit")
    public ResponseEntity<HttpStatus> putMoneyIntoTheAccount(@RequestBody CardTransactionDepositAndWithdrawDTO dto){
        return new ResponseEntity<>(cardService.deposit(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdrawMoneyFromTheAccount(@RequestBody CardTransactionDepositAndWithdrawDTO dto){
        return new ResponseEntity<>(cardService.withdraw(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
