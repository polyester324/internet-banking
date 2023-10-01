package com.tms.controller;

import com.tms.domain.Card;
import com.tms.service.CardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {
    public final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public Card getClientById(@RequestParam Long id){
        return cardService.getCardById(id);
    }

    @PostMapping
    public Boolean createClient(@RequestBody Card card){
        return cardService.createCard(card);
    }

    @PutMapping("/balance")
    public Boolean updateCardBalance(@RequestBody Card card){
        return cardService.updateCardBalance(card);
    }

    @PutMapping("/money_currency")
    public Boolean updateCardMoneyCurrency(@RequestBody Card card){
        return cardService.updateCardMoneyCurrency(card);
    }
}
