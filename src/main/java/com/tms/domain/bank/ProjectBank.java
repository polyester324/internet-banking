package com.tms.domain.bank;

import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectBank extends Bank implements BankFactory {
    private final String BANK_NAME = "Project bank";
    private final BigDecimal COMMISSION = BigDecimal.valueOf(0.0);

    @Override
    public Card createCard() {
        return new ProjectCard();
    }
}
