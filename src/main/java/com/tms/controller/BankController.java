package com.tms.controller;

import com.tms.domain.bank.Bank;
import com.tms.dtos.BankUpdateDTO;
import com.tms.dtos.CardCreationDTO;
import com.tms.service.BankService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/bank")
public class BankController {

    public final BankService bankService;

    /**
     * getBankById is a GET method that shows the bank by requested id in url path
     * @return 200 ok if bank was found and 409 conflict otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable ("id") Long id){
        return new ResponseEntity<>(bankService.getBankById(id), HttpStatus.OK);
    }

    /**
     * getAll is a GET method that shows all banks from db
     * @return 200 ok
     */
    @GetMapping
    public ResponseEntity<List<Bank>> getAll(){
        log.info("getAll Bank method working!");
        List<Bank> resultList = bankService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    /**
     * createCard is a POST method that creates the card
     * @return 201 created if card was created and 409 conflict otherwise
     */
    @PostMapping("/create-card/{id}")
    public ResponseEntity<HttpStatus> createCard(@PathVariable ("id") Long id, @RequestBody CardCreationDTO dto){
        return new ResponseEntity<>(bankService.createCard(dto.getBankName(), dto.getMoneyCurrency(), id) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * updateBank is a PUT method that updates the bank by given json data
     * @return 204 no content if bank was updated and 409 conflict otherwise
     */
    @PutMapping
    public ResponseEntity<HttpStatus> updateBank(@RequestBody BankUpdateDTO dto){
        return new ResponseEntity<>(bankService.updateBank(dto.getId(), dto.getBankName(), dto.getCommission(), dto.getCreated()) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
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
