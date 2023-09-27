package com.tms.controller;

import com.tms.domain.Card;
import com.tms.service.CardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {
    public final CardService CARD_SERVICE;

    public CardController(CardService cardService) {
        CARD_SERVICE = cardService;
    }

    @GetMapping
    public Card getClientById(@RequestParam Long id){
        return CARD_SERVICE.getCardById(id);
    }

    @PostMapping
    public Boolean createClient(@RequestBody Card card){
        return CARD_SERVICE.createCard(card);
    }

    @PutMapping("/balance")
    public Boolean updateCardBalance(@RequestBody Card card){
        return CARD_SERVICE.updateCardBalance(card);
    }

    @PutMapping("/money_currency")
    public Boolean updateCardMoneyCurrency(@RequestBody Card card){
        return CARD_SERVICE.updateCardMoneyCurrency(card);
    }
}
