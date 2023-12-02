package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

/**
 * BelarusCard is a class, that inherits Card
 */
@Component
@DiscriminatorValue("Belarus bank")
@Entity
public class BelarusCard extends Card {
    public BelarusCard() {
        super();
    }
}
