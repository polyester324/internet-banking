package com.tms.service;

import com.tms.domain.bank.*;
import com.tms.exceptions.NoSuchBankException;
import com.tms.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankService {
    public Bank getBankByName(String bankName) throws NoSuchBankException {
        switch (bankName) {
            case "Alpha bank" -> {
                return new AlphaBank();
            }
            case "Belarus bank" -> {
                return new BelarusBank();
            }
            case "Prior bank" -> {
                return new PriorBank();
            }
            case "Project bank" -> {
                return new ProjectBank();
            }
        }
        log.info(String.format("Bank with name: %s does not exist", bankName));
        throw new NoSuchBankException();
    }
}
