package com.tms.controller;

import com.tms.domain.Card;
import com.tms.service.CardService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/operation")
public class OperationController {

    public final CardService CARD_SERVICE;

    public OperationController(CardService cardService) {
        CARD_SERVICE = cardService;
    }

    /**
     *     transfer принимает из json id карты отправителя и id карты получателя, и сумму перевода из url,
     *     по id ищет текущий баланс карт, идет в базу данных и изменяет балансы,
     *     делает проверку RuntimeException,
     *     @return true, если деньги были переведены
     */
    @PutMapping("/transfer")
    public Boolean transfer(@RequestBody Card[] cards, @RequestParam BigDecimal amount){
        try{
            cards[0].setBalance(CARD_SERVICE.getCardById(cards[0].getId()).getBalance());
            cards[1].setBalance(CARD_SERVICE.getCardById(cards[1].getId()).getBalance());
            cards[0].setBalance(cards[0].getBalance().subtract(amount));
            cards[1].setBalance(cards[1].getBalance().add(amount));
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return false;
        }
        return CARD_SERVICE.updateCardBalance(cards[0]) && CARD_SERVICE.updateCardBalance(cards[1]);
    }
}
