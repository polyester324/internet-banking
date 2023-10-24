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

/**
 * TransactionController is a class controller that responds to incoming requests from the path("/transaction")
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    public final CardService cardService;

    public TransactionController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * transferMoneyBetweenTwoClients is a PUT method that transfer money from one card to another from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/transfer")
    public ResponseEntity<HttpStatus> transferMoneyBetweenTwoClients(@RequestBody CardTransactionTransferDTO dto){
        return new ResponseEntity<>(cardService.transfer(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * putMoneyIntoTheAccount is a PUT method that puts money on the card from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/deposit")
    public ResponseEntity<HttpStatus> putMoneyIntoTheAccount(@RequestBody CardTransactionDepositAndWithdrawDTO dto){
        return new ResponseEntity<>(cardService.deposit(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * withdrawMoneyFromTheAccount is a PUT method that withdraws money from the card from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdrawMoneyFromTheAccount(@RequestBody CardTransactionDepositAndWithdrawDTO dto){
        return new ResponseEntity<>(cardService.withdraw(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
