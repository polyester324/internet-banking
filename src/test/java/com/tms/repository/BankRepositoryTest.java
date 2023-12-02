package com.tms.repository;

import com.tms.domain.bank.Bank;
import com.tms.domain.bank.ProjectBank;
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
class BankRepositoryTest {
    @Autowired
    private BankRepository bankRepository;
    static Bank bankInfo;

    @BeforeAll
    static void beforeAll() {
        bankInfo = new ProjectBank();
        bankInfo.setBankName("test Bank");
        bankInfo.setCommission(BigDecimal.valueOf(0.1));
        bankInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void findAllTest() {
        bankRepository.save(bankInfo);
        List<Bank> newList = bankRepository.findAll();
        assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        Bank saved = bankRepository.save(bankInfo);
        Optional<Bank> newBank = bankRepository.findById(saved.getId());
        Assertions.assertTrue(newBank.isPresent());
    }

    @Test
    void saveTest() {
        List<Bank> oldList = bankRepository.findAll();
        bankRepository.save(bankInfo);
        List<Bank> newList = bankRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        Bank bankSaved = bankRepository.save(bankInfo);
        bankSaved.setBankName("UPDATED_TEST_BANK");
        bankSaved.setCommission(BigDecimal.valueOf(0.2));
        LocalDateTime time = LocalDateTime.now();
        bankSaved.setCreated(Timestamp.valueOf(time));
        Bank bankUpdated = bankRepository.saveAndFlush(bankSaved);
        Assertions.assertEquals(bankUpdated.getBankName(), "UPDATED_TEST_BANK");
        Assertions.assertEquals(bankUpdated.getCommission(), BigDecimal.valueOf(0.2));
        Assertions.assertEquals(bankUpdated.getCreated(), Timestamp.valueOf(time));
    }

    @Test
    void deleteTest() {
        Bank bankSaved = bankRepository.save(bankInfo);
        bankRepository.delete(bankSaved);
        Optional<Bank> bank = bankRepository.findById(bankSaved.getId());
        Assertions.assertFalse(bank.isPresent());
    }
}