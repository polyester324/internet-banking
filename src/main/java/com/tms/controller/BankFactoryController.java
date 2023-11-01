package com.tms.controller;

import com.tms.domain.card.Card;
import com.tms.dtos.CardCreationDTO;
import com.tms.service.BankFactoryService;
import com.tms.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bank-operations")
public class BankFactoryController {

    public final BankFactoryService bankFactoryService;

    @PostMapping("/create-card")
    public ResponseEntity<HttpStatus> createCard(@RequestBody CardCreationDTO card){
        return new ResponseEntity<>(bankFactoryService.createCard(card.getBankName(), card.getMoneyCurrency(),card.getClientId()) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
