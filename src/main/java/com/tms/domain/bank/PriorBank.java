package com.tms.domain.bank;

import com.tms.domain.card.Card;
import com.tms.domain.card.PriorCard;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * PriorBank is a class, that inherits Bank
 */
@Component
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Prior bank")
@Data
@Entity
public class PriorBank extends Bank implements BankFactory {
    public PriorBank() {
        super();
    }

    @Override
    public Card createCard() {
        return new PriorCard();
    }
}
