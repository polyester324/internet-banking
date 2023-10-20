package com.tms.controller;

import com.tms.domain.Card;
import com.tms.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Optional;

@RestController
@RequestMapping("/card")
public class CardController {
    public final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getClientById(@PathVariable ("id") Long id){
        Optional<Card> card = cardService.getCardById(id);
        return card.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createClient(@RequestBody Card card){
        return new ResponseEntity<>(cardService.createCard(card) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable ("id") Long id){
        return new ResponseEntity<>(cardService.deleteCardById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
