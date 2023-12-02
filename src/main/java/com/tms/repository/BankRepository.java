package com.tms.repository;

import com.tms.domain.bank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    /**
     * findBankByBankName is a method, that finds bank by bank name
     * @return Bank
     */
    Bank findBankByBankName(String bankName);

    /**
     * findBankCommissionByBankName is a method, that finds bank commission by bank name
     * @return BigDecimal
     */
    @Query("select commission from banks where bankName = :bankName")
    BigDecimal findBankCommissionByBankName(String bankName);
}
