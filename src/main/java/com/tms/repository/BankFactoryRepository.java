package com.tms.repository;

import com.tms.domain.bank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankFactoryRepository extends JpaRepository<Bank, Long> {
}
