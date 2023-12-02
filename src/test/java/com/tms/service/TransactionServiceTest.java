package com.tms.service;

import com.tms.domain.Client;
import com.tms.domain.bank.Bank;
import com.tms.domain.bank.ProjectBank;
import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import com.tms.repository.BankRepository;
import com.tms.repository.CardRepository;
import com.tms.repository.TransactionRepository;
import com.tms.security.service.SecurityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;
    @Mock
    CardRepository cardRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    BankRepository bankRepository;
    @Mock
    SecurityService securityService;
    @Mock
    ClientService clientService;

    static final String DIRECTORY_PATH = "checks/56-TESTER-TESTER";
    static List<Client> clientList = null;
    static Card card = null;
    static Client client = null;
    static Bank bank = null;
    static Long clientId = 56L;
    static String moneyCurrency = "USD";
    static BufferedWriter bufferedWriter;

    static {
        try {
            File file = new File("checks/56-TESTER-TESTER/check_2524_20231130_0000-00_17d68967-a9ce-4ae6-b5f2-d9366f591168.txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Integer checkNumber = 123435;
    static String type = "Project bank";
    static String bankNameSender = "Project bank";
    static String bankNameReceiver = "Project bank";
    static String cardSender = "0000-0000-0000-0000";
    static String cardReceiver = "1111-1111-1111-1111";
    static BigDecimal amount = BigDecimal.valueOf(0);

    @BeforeAll
    static void beforeAll() {
        clientList = new ArrayList<>();
        client = new Client();
        client.setId(clientId);
        client.setFirstName("TESTER");
        client.setLastName("TESTER");
        client.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        clientList.add(client);

        card = new ProjectCard();
        card.setCardNumber("0000-0000-0000-0000");
        card.setClientId(clientId);
        card.setBalance(BigDecimal.valueOf(0));
        card.setMoneyCurrency("USD");
        card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        card.setCardType("Project bank");

        bank = new ProjectBank();
        bank.setBankName("test Bank");
        bank.setCommission(BigDecimal.valueOf(0.1));
        bank.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @AfterAll
    static void cleanUp() throws IOException {
        Path path = Paths.get(DIRECTORY_PATH);
        Files.walk(path)
                .sorted((a, b) -> b.toString().length() - a.toString().length())
                .map(Path::toFile)
                .forEach(File::delete);
    }
    @Test
    void depositTest() {
        when(cardRepository.findCardMoneyCurrencyByCardNumber(any())).thenReturn(moneyCurrency);
        when(cardRepository.findCardTypeByCardNumber(any())).thenReturn(type);
        when(clientService.getClientById(any())).thenReturn(Optional.ofNullable(client));
        when(cardRepository.findCardByCardNumber(any())).thenReturn(card);
        List<String> list = transactionService.deposit(card.getCardNumber(), card.getBalance(), card.getMoneyCurrency());
        Assertions.assertNotNull(list.get(0));
        Assertions.assertNotNull(list.get(1));
        verify(transactionRepository, times(1)).deposit(any(), any(BigDecimal.class));
    }

    @Test
    void withdrawTest() {
        when(cardRepository.findCardMoneyCurrencyByCardNumber(any())).thenReturn(moneyCurrency);
        when(cardRepository.findCardTypeByCardNumber(any())).thenReturn(type);
        when(clientService.getClientById(any())).thenReturn(Optional.ofNullable(client));
        when(cardRepository.findCardByCardNumber(any())).thenReturn(card);
        List<String> list = transactionService.withdraw(card.getCardNumber(), card.getBalance(), card.getMoneyCurrency());
        Assertions.assertNotNull(list.get(0));
        Assertions.assertNotNull(list.get(1));
        verify(transactionRepository, times(1)).withdraw(any(), any(BigDecimal.class));
    }

    @Test
    void transferTest() {
        when(securityService.checkAccessById(anyLong())).thenReturn(true);
        when(cardRepository.findCardMoneyCurrencyByCardNumber(any())).thenReturn(moneyCurrency);
        when(cardRepository.findCardTypeByCardNumber(any())).thenReturn(type);
        when(bankRepository.findBankCommissionByBankName(any())).thenReturn(bank.getCommission());
        when(clientService.getClientById(any())).thenReturn(Optional.ofNullable(client));
        when(cardRepository.findCardByCardNumber(any())).thenReturn(card);
        List<String> list = transactionService.transfer(card.getCardNumber(), cardReceiver, card.getBalance(), card.getMoneyCurrency());
        Assertions.assertNotNull(list.get(0));
        Assertions.assertNotNull(list.get(1));
        verify(transactionRepository, times(1)).withdraw(any(), any(BigDecimal.class));
    }

    @Test
    void makeRandomCheckNumberTest() {
        Integer result = transactionService.makeRandomCheckNumber();
        System.out.println(result);
        assertTrue(result >= 0);
    }

    @Test
    void writeCheckTest() {
        try {
            transactionService.writeCheck(bufferedWriter, checkNumber, type, bankNameSender, bankNameReceiver, cardSender, cardReceiver, amount, moneyCurrency, bank.getCommission());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void makeCheckForDepositAndWithdrawTest() {
        when(clientService.getClientById(any())).thenReturn(Optional.ofNullable(client));
        when(cardRepository.findCardByCardNumber(any())).thenReturn(card);
        List<String> list = transactionService.makeCheckForDepositAndWithdraw(cardSender, amount, moneyCurrency, bankNameSender, type);
        Assertions.assertNotNull(list.get(0));
        Assertions.assertNotNull(list.get(1));
    }

    @Test
    void makeCheckForTransferTest() {
        when(clientService.getClientById(any())).thenReturn(Optional.ofNullable(client));
        when(cardRepository.findCardByCardNumber(any())).thenReturn(card);
        List<String> list = transactionService.makeCheckForTransfer(cardSender, cardReceiver, amount, moneyCurrency, bankNameSender, bankNameReceiver, type, bank.getCommission());
        Assertions.assertNotNull(list.get(0));
        Assertions.assertNotNull(list.get(1));
    }

    @Test
    void makeUniqueFileTest() throws IOException {
        when(clientService.getClientById(any())).thenReturn(Optional.ofNullable(client));
        when(cardRepository.findCardByCardNumber(any())).thenReturn(card);
        File resultFile = transactionService.makeUniqueFile(any(), checkNumber);

        assertNotNull(resultFile);
        assertTrue(resultFile.exists());
        assertTrue(resultFile.isFile());

        Files.deleteIfExists(Paths.get(resultFile.getPath()));
    }

    @Test
    void equalizationCoefficientToOneExchangeRateTest() {
        double result = transactionService.equalizationCoefficientToOneExchangeRate(moneyCurrency, moneyCurrency);
        assertTrue(result > 0);
    }
}