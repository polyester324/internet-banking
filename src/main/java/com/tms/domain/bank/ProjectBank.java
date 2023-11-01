package com.tms.domain.bank;

import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;

import java.math.BigDecimal;

public class ProjectBank extends Bank implements BankFactory {
    private final String BANK_NAME = "Project bank";
    private final BigDecimal COMMISSION = BigDecimal.valueOf(0.0);

    @Override
    public Card createCard() {
        return new ProjectCard();
    }
}
