package com.tms.service;

import com.tms.domain.bank.Bank;
import com.tms.domain.bank.ProjectBank;
import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import com.tms.repository.BankRepository;
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
class BankServiceTest {

    @InjectMocks
    BankService bankService;
    @Mock
    BankRepository bankRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    SecurityService securityService;
    static List<Bank> bankList = null;
    static Bank bank = null;
    static Card card = null;
    static Long bankId = 1L;
    static Long clientId = 1L;
    static String moneyCurrency = "USD";
    static List<String> cardNumbers = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        bankList = new ArrayList<>();
        bank = new ProjectBank();
        bank.setBankName("Project bank");
        bank.setCommission(BigDecimal.valueOf(0.1));
        bank.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        bankList.add(bank);

        card = new ProjectCard();
        card.setCardNumber("0000-0000-0000-0000");

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getBankByIdTest() {
        Mockito.when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));

        Optional<Bank> result = Optional.ofNullable(bankService.getBankById(bankId));
        Mockito.verify(bankRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.orElseThrow());
    }

    @Test
    void getAllTest() {
        Mockito.when(bankRepository.findAll()).thenReturn(bankList);
        List<Bank> resultList = bankService.getAll();
        Mockito.verify(bankRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void updateBankTest() {
        Mockito.when(bankRepository.saveAndFlush(any())).thenReturn(bank);
        Mockito.when(bankRepository.findBankByBankName(any())).thenReturn(bank);
        Boolean result = bankService.updateBank(bank.getId(), bank.getBankName(), bank.getCommission(), bank.getCreated());
        Assertions.assertTrue(result);
    }

    @Test
    void deleteBankTest() {
        bankService.deleteBankById(bankId);
        Mockito.verify(bankRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    void createCardTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Mockito.when(bankRepository.findBankByBankName(any())).thenReturn(bank);
        Mockito.when(cardRepository.findAllCardNumbers()).thenReturn(cardNumbers);
        Boolean result = bankService.createCard(bank.getBankName(), moneyCurrency, clientId);
        Assertions.assertTrue(result);
    }

    @Test
    void getBankByBankNameTest() {
        Mockito.when(bankRepository.findBankByBankName(any())).thenReturn(bank);
        Optional<Bank> result = Optional.ofNullable(bankService.getBankByBankName(bank.getBankName()));
        Assertions.assertNotNull(result.orElseThrow());
    }

}