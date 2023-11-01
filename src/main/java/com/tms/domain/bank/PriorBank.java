package com.tms.domain.bank;

import com.tms.domain.card.Card;
import com.tms.domain.card.PriorCard;

import java.math.BigDecimal;

public class PriorBank extends Bank implements BankFactory {
    private final String BANK_NAME = "Prior bank";
    private final BigDecimal COMMISSION = BigDecimal.valueOf(0.5);

    @Override
    public Card createCard() {
        return new PriorCard();
    }
}
