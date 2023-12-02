package com.tms.domain.bank;

import com.tms.domain.card.BelarusCard;
import com.tms.domain.card.Card;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * BelarusBank is a class, that inherits Bank
 */
@Component
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Belarus bank")
@Data
@Entity
public class BelarusBank extends Bank implements BankFactory {
    public BelarusBank() {
        super();
    }

    @Override
    public Card createCard() {
        return new BelarusCard();
    }
}
