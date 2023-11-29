package com.tms.controller;

import com.tms.dtos.CardTransactionDepositAndWithdrawDTO;
import com.tms.dtos.CardTransactionTransferDTO;
import com.tms.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * TransactionController is a class controller that responds to incoming requests from the path("/transaction")
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    public final TransactionService transactionService;
    private final Path ROOT_FILE_PATH = Paths.get("");

    /**
     * transferMoneyBetweenTwoClients is a PUT method that transfer money from one card to another from json data
     * @return 204 no content if operation was successful and 409 conflict otherwise
     */
    @PutMapping("/transfer")
    public ResponseEntity<Resource> transferMoneyBetweenTwoClients(@Valid @RequestBody CardTransactionTransferDTO dto){
        try {
            return printCheck(transactionService.transfer(dto.getCardSender(), dto.getCardReceiver(), dto.getAmount(), dto.getMoneyCurrency()));
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
            return printCheck(transactionService.deposit(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()));
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
            return printCheck(transactionService.withdraw(dto.getCard(), dto.getAmount(), dto.getMoneyCurrency()));
        } catch (Exception e){
            log.info("withdraw operation failed");
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * printCheck is a method that prints check for every transaction operation
     * @return resource, headers, HttpStatus.NO_CONTENT if operation was successful and 409 conflict otherwise
     */
    public ResponseEntity<Resource> printCheck(String filename) {
        Path path = ROOT_FILE_PATH.resolve(filename);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            log.info(String.format("failed to return check, exception: %s", e));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
