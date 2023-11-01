package com.tms.domain.bank;

import com.tms.domain.card.BelarusCard;
import com.tms.domain.card.Card;

import java.math.BigDecimal;

public class BelarusBank extends Bank implements BankFactory {
    private final String BANK_NAME = "Belarus bank";
    private final BigDecimal COMMISSION = BigDecimal.valueOf(0.1);

    @Override
    public Card createCard() {
        return new BelarusCard();
    }
}
