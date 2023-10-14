package com.tms.service;

import com.tms.domain.Card;
import com.tms.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Boolean createCard(Card card){
        card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return cardRepository.createCard(card);
    }

    public Optional<Card> getCardById(Long id){
        return cardRepository.getCardById(id);
    }

    public boolean deleteCardById(Long id){
        return cardRepository.deleteCardById(id);
    }
}
