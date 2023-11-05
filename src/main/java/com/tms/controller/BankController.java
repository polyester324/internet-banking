package com.tms.controller;

import com.tms.domain.bank.Bank;
import com.tms.dtos.CardCreationDTO;
import com.tms.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bank")
public class BankController {

    public final BankService bankService;

    /**
     * getBankById is a GET method that shows the bank by requested id in url path
     * @return 200 ok if bank was found and 409 conflict otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable ("id") Long id){
        try {
            Bank bank = bankService.getBankById(id);
            return new ResponseEntity<>(bank, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * getAll is a GET method that shows all banks from db
     * @return 200 ok
     */
    @GetMapping()
    public ResponseEntity<List<Bank>> getAll(){
        log.info("getAll Bank method working!");
        List<Bank> resultList = bankService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    /**
     * createBank is a POST method that creates the bank by given json data
     * @return 201 created if bank was created and 409 conflict otherwise
     */
    @PostMapping
    public ResponseEntity<HttpStatus> createBank(@RequestBody Bank bank){
        return new ResponseEntity<>(bankService.createBank(bank) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * createCard is a POST method that creates the card
     * @return 201 created if card was created and 409 conflict otherwise
     */
    @PostMapping("/create-card")
    public ResponseEntity<HttpStatus> createCard(@RequestBody CardCreationDTO card){
        return new ResponseEntity<>(bankService.createCard(card.getBankName(), card.getMoneyCurrency(),card.getClientId()) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * updateBank is a PUT method that updates the bank by given json data
     * @return 204 no content if bank was updated and 409 conflict otherwise
     */
    @PutMapping
    public ResponseEntity<HttpStatus> updateBank(@RequestBody Bank bank){
        return new ResponseEntity<>(bankService.updateBank(bank) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * deleteBank is a DELETE method that deletes the bank by requested id in url path
     * @return 204 no content if bank was deleted and 409 conflict otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBank(@PathVariable ("id") Long id){
        return new ResponseEntity<>(bankService.deleteBankById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
