package com.tms.domain.bank;

import com.tms.domain.card.Card;
import com.tms.domain.card.PriorCard;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class PriorBank extends Bank implements BankFactory {
    private final String BANK_NAME = "Prior bank";
    private final BigDecimal COMMISSION = BigDecimal.valueOf(0.5);

    @Override
    public Card createCard() {
        return new PriorCard();
    }
}
