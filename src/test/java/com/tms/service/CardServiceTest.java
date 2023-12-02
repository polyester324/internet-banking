package com.tms.service;

import com.tms.domain.bank.Bank;
import com.tms.domain.bank.BankFactory;
import com.tms.domain.bank.ProjectBank;
import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import com.tms.repository.CardRepository;
import com.tms.security.service.SecurityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;

    @Mock
    SecurityService securityService;

    @Mock
    BankService bankService;


    static List<Card> cardList = null;
    static Card card = null;
    static BankFactory bank = null;

    static Long cardId = 29L;
    static Long clientId = 56L;

    @BeforeAll
    static void beforeAll() {
        cardList = new ArrayList<>();
        card = new ProjectCard();
        card.setCardNumber("1111-1111-1111-1111");
        card.setClientId(clientId);
        card.setBalance(BigDecimal.valueOf(10000));
        card.setMoneyCurrency("USD");
        card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        card.setCardType("Project bank");
        cardList.add(card);

        bank = new ProjectBank();
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getCardByIdTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Mockito.when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        Optional<Card> result = Optional.ofNullable(cardService.getCardById(cardId));
        Mockito.verify(cardRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.orElseThrow());
    }

    @Test
    void getAllTest() {
        Mockito.when(cardRepository.findAll()).thenReturn(cardList);

        List<Card> resultList = cardService.getAll();
        Mockito.verify(cardRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void createCardTest() {
        Mockito.when(cardRepository.save(any())).thenReturn(card);
        Mockito.when(bankService.getBankByBankName(any())).thenReturn((Bank) bank);
        Boolean result = cardService.createCard(card.getCardNumber(), card.getClientId(), card.getBalance(), card.getMoneyCurrency(), card.getCardType());
        Assertions.assertTrue(result);
        Mockito.verify(cardRepository, Mockito.times(1)).save(any());

    }

    @Test
    void updateCardTest() {
        Mockito.when(cardRepository.saveAndFlush(any())).thenReturn(card);
        Mockito.when(bankService.getBankByBankName(any())).thenReturn((Bank) bank);
        Boolean result = cardService.updateCard(card.getId(), card.getCardNumber(), card.getClientId(), card.getBalance(), card.getMoneyCurrency(), card.getCardType());
        Mockito.verify(cardRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
        Mockito.verify(cardRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    void deleteCardTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        cardService.deleteCardById(cardId);
        Mockito.verify(cardRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    void getCardByCardNumber() {
        Mockito.when(cardRepository.findCardByCardNumber(any())).thenReturn(card);

        Optional<Card> result = Optional.ofNullable(cardService.getCardByCardNumber(card.getCardNumber()));
        Assertions.assertNotNull(result.orElseThrow());
    }
}