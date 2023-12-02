package com.tms.repository;

import com.tms.domain.Investment;
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
class InvestmentRepositoryTest {
    @Autowired
    private InvestmentRepository investmentRepository;
    static Investment investmentInfo;

    @BeforeAll
    static void beforeAll() {
        investmentInfo = new Investment();
        investmentInfo.setInvestmentNumber("000000000");
        investmentInfo.setCardId(29L);
        investmentInfo.setBankId(1L);
        investmentInfo.setMoneyCurrency("USD");
        investmentInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        investmentInfo.setExpired(Timestamp.valueOf(LocalDateTime.now()));
        investmentInfo.setInvestedAmount(BigDecimal.valueOf(0));
        investmentInfo.setExpectedAmount(BigDecimal.valueOf(0));
        investmentInfo.setTime("ONE_YEAR");
        investmentInfo.setClientId(56L);
    }

    @Test
    void findAllTest() {
        investmentRepository.save(investmentInfo);
        List<Investment> newList = investmentRepository.findAll();
        assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        Investment savedInvestment = investmentRepository.save(investmentInfo);
        Optional<Investment> newBank = investmentRepository.findById(savedInvestment.getId());
        Assertions.assertTrue(newBank.isPresent());
    }

    @Test
    void saveTest() {
        List<Investment> oldList = investmentRepository.findAll();
        investmentRepository.save(investmentInfo);
        List<Investment> newList = investmentRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        Investment investmentSaved = investmentRepository.save(investmentInfo);
        investmentSaved.setInvestmentNumber("000000001");
        investmentSaved.setTime("TWO_YEARS");
        LocalDateTime time = LocalDateTime.now();
        investmentSaved.setCreated(Timestamp.valueOf(time));
        Investment investmentUpdated = investmentRepository.saveAndFlush(investmentSaved);
        Assertions.assertEquals(investmentUpdated.getInvestmentNumber(), "000000001");
        Assertions.assertEquals(investmentUpdated.getTime(), "TWO_YEARS");
        Assertions.assertEquals(investmentUpdated.getCreated(), Timestamp.valueOf(time));
    }

    @Test
    void deleteTest() {
        Investment bankSaved = investmentRepository.save(investmentInfo);
        investmentRepository.delete(bankSaved);
        Optional<Investment> bank = investmentRepository.findById(bankSaved.getId());
        Assertions.assertFalse(bank.isPresent());
    }
}