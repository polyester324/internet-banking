package com.tms.service;

import com.tms.domain.Card;
import com.tms.repository.CardRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Boolean createCard(Card card){
        return cardRepository.createCard(card);
    }

    public Optional<Card> getCardById(Long id){
        return cardRepository.getCardById(id);
    }

    public Boolean updateCardBalance(Card card){
        return cardRepository.updateCardBalance(card);
    }

    public Boolean updateCardMoneyCurrency(Card card){
        return cardRepository.updateCardMoneyCurrency(card);
    }
}
