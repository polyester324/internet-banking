package com.tms.domain.bank;

import com.tms.domain.card.AlphaCard;
import com.tms.domain.card.Card;

import java.math.BigDecimal;

public class AlphaBank extends Bank implements BankFactory{
    private final String BANK_NAME = "Alpha bank";
    private final BigDecimal COMMISSION = BigDecimal.valueOf(0.2);

    @Override
    public Card createCard() {
        return new AlphaCard();
    }
}
