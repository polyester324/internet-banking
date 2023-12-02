package com.tms.repository;

import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;
    static Card cardInfo;

    @BeforeAll
    static void beforeAll() {
        cardInfo = new ProjectCard();
        cardInfo.setCardNumber("0000-0000-0000-0000");
        cardInfo.setClientId(56L);
        cardInfo.setBalance(BigDecimal.valueOf(10000));
        cardInfo.setMoneyCurrency("USD");
        cardInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        cardInfo.setCardType("Project bank");
    }

    @Test
    void findAllTest() {
        cardRepository.save(cardInfo);
        List<Card> newList = cardRepository.findAll();
        assertNotNull(newList);
    }

    @Test
    void findCardByIdTest() {
        Card saved = cardRepository.save(cardInfo);
        Optional<Card> newCard = cardRepository.findById(saved.getId());
        Assertions.assertTrue(newCard.isPresent());
    }

    @Test
    void saveCardTest() {
        List<Card> oldList = cardRepository.findAll();
        cardRepository.save(cardInfo);
        List<Card> newList = cardRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateCardTest() {
        Card cardSaved = cardRepository.save(cardInfo);
        cardSaved.setCardNumber("2222-2222-2222-2222");
        cardSaved.setBalance(BigDecimal.valueOf(20000));
        LocalDateTime time = LocalDateTime.now();
        cardSaved.setCreated(Timestamp.valueOf(time));
        Card cardUpdated = cardRepository.saveAndFlush(cardSaved);
        Assertions.assertEquals(cardUpdated.getCardNumber(), "2222-2222-2222-2222");
        Assertions.assertEquals(cardUpdated.getBalance(), BigDecimal.valueOf(20000));
        Assertions.assertEquals(cardUpdated.getCreated(), Timestamp.valueOf(time));
    }

    @Test
    void deleteCardTest() {
        Card cardSaved = cardRepository.save(cardInfo);
        cardRepository.delete(cardSaved);
        Optional<Card> card = cardRepository.findById(cardSaved.getId());
        Assertions.assertFalse(card.isPresent());
    }
}