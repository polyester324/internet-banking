package com.tms.domain.bank;

import com.tms.domain.card.AlphaCard;
import com.tms.domain.card.Card;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Alpha bank")
@Data
@Entity
public class AlphaBank extends Bank implements BankFactory{
    public AlphaBank() {
        super();
    }

    @Override
    public Card createCard() {
        return new AlphaCard();
    }
}
