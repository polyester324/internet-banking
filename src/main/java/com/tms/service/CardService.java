package com.tms.service;

import com.tms.domain.Card;
import com.tms.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    private final CardRepository CARD_REPOSITORY;

    public CardService(CardRepository cardRepository) {
        CARD_REPOSITORY = cardRepository;
    }

    public Boolean createCard(Card card){
        return CARD_REPOSITORY.createCard(card);
    }

    public Card getCardById(Long id){
        return CARD_REPOSITORY.getCardById(id);
    }

    public Boolean updateCardBalance(Card card){
        return CARD_REPOSITORY.updateCardBalance(card);
    }

    public Boolean updateCardMoneyCurrency(Card card){
        return CARD_REPOSITORY.updateCardMoneyCurrency(card);
    }
}
