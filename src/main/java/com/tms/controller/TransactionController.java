package com.tms.controller;

import com.tms.dtos.CardTransactionDepositAndWithdrawDTO;
import com.tms.dtos.CardTransactionTransferDTO;
import com.tms.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;

/**
 * TransactionController is a class controller that responds to incoming requests from the path("/transaction")
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/transaction")
public class TransactionController {
    public final TransactionService transactionService;
    public final FileController fileController;

    /**
     * transferMoneyBetweenTwoClients is a PUT method that transfer money from one card to another from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/transfer")
    public ResponseEntity<Resource> transferMoneyBetweenTwoClients(@Valid @RequestBody CardTransactionTransferDTO dto){
        try {
            ArrayList<String> list = (ArrayList<String>) transactionService.transfer(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount(), dto.getMoneyCurrency());
            return fileController.getFile(list.get(0), list.get(1));
        } catch (Exception e){
            log.info("transfer operation failed");
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * putMoneyIntoTheAccount is a PUT method that puts money on the card from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/deposit")
    public ResponseEntity<Resource> putMoneyIntoTheAccount(@Valid @RequestBody CardTransactionDepositAndWithdrawDTO dto){
        try {
            ArrayList<String> list = (ArrayList<String>) transactionService.deposit(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency());
            return fileController.getFile(list.get(0), list.get(1));
        } catch (Exception e){
            log.info("deposit operation failed");
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * withdrawMoneyFromTheAccount is a PUT method that withdraws money from the card from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/withdraw")
    public ResponseEntity<Resource> withdrawMoneyFromTheAccount(@Valid @RequestBody CardTransactionDepositAndWithdrawDTO dto){
        try {
            ArrayList<String> list = (ArrayList<String>) transactionService.withdraw(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency());
            return fileController.getFile(list.get(0), list.get(1));
        } catch (Exception e){
            log.info("withdraw operation failed");
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
