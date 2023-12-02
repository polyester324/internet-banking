package com.tms.service;

import com.tms.domain.Investment;
import com.tms.domain.bank.Bank;
import com.tms.domain.bank.ProjectBank;
import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import com.tms.repository.InvestmentRepository;
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
class InvestmentServiceTest {

    @InjectMocks
    InvestmentService investmentService;
    @Mock
    CardService cardService;
    @Mock
    BankService bankService;
    @Mock
    TransactionService transactionService;
    @Mock
    InvestmentRepository investmentRepository;
    @Mock
    SecurityService securityService;

    static List<Investment> investmentList = null;
    static Investment investment = null;
    static Card card = null;
    static Bank bank = null;
    static Long clientId = 56L;
    static Long cardId = 29L;
    static Long bankId = 1L;

    @BeforeAll
    static void beforeAll() {
        investmentList = new ArrayList<>();
        investment = new Investment();
        investment.setInvestmentNumber("000000000");
        investment.setCardId(cardId);
        investment.setBankId(bankId);
        investment.setMoneyCurrency("USD");
        investment.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        investment.setExpired(Timestamp.valueOf(LocalDateTime.now()));
        investment.setInvestedAmount(BigDecimal.valueOf(0));
        investment.setExpectedAmount(BigDecimal.valueOf(0));
        investment.setTime("ONE_YEAR");
        investment.setClientId(clientId);
        investmentList.add(investment);

        card = new ProjectCard();
        card.setCardNumber("0000-0000-0000-0000");
        card.setClientId(clientId);
        card.setBalance(BigDecimal.valueOf(10000));
        card.setMoneyCurrency("USD");
        card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        card.setCardType("Project bank");

        bank = new ProjectBank();
        bank.setBankName("Project Bank");
        bank.setCommission(BigDecimal.valueOf(0.1));
        bank.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getInvestmentByIdTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Mockito.when(investmentRepository.findById(anyLong())).thenReturn(Optional.of(investment));
        Optional<Investment> result = investmentService.getInvestmentById(clientId);
        Mockito.verify(investmentRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.orElseThrow());
    }

    @Test
    void getAllTest() {
        Mockito.when(investmentRepository.findAll()).thenReturn(investmentList);
        List<Investment> resultList = investmentService.getAll();
        Mockito.verify(investmentRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void makeAnInvestmentTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Mockito.when(investmentRepository.save(any())).thenReturn(investment);
        Mockito.when(cardService.getCardByCardNumber(any())).thenReturn(card);
        Mockito.when(bankService.getBankByBankName(any())).thenReturn(bank);
        Boolean result = investmentService.makeAnInvestment(card.getCardNumber(), bank.getBankName(), investment.getMoneyCurrency(), investment.getTime(), investment.getInvestedAmount(), clientId);
        Assertions.assertTrue(result);
        Mockito.verify(transactionService, Mockito.times(1)).withdraw(any(), any(BigDecimal.class), any());
    }

    @Test
    void createInvestmentTest() {
        Mockito.when(investmentRepository.save(any())).thenReturn(investment);
        Boolean result = investmentService.createInvestment(investment);
        Mockito.verify(investmentRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }

    @Test
    void updateInvestmentTest() {
        Mockito.when(investmentRepository.saveAndFlush(any())).thenReturn(investment);
        Boolean result = investmentService.updateInvestment(investment);
        Mockito.verify(investmentRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
    }

    @Test
    void deleteInvestmentTest() {
        investmentService.deleteInvestmentById(clientId);
        Mockito.verify(investmentRepository, Mockito.times(1)).deleteById(anyLong());
    }
    @Test
    void getAllInvestmentNumbers() {
        List<String> result = investmentService.getAllInvestmentNumbers();
        Assertions.assertNotNull(result);
    }

    @Test
    void deleteAndAccrueExpiredInvestments() {
        investmentService.deleteAndAccrueExpiredInvestments();
    }
}