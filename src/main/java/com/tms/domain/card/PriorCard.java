package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

/**
 * PriorCard is a class, that inherits Card
 */
@Component
@DiscriminatorValue("Prior bank")
@Entity
public class PriorCard extends Card {
    public PriorCard() {
        super();
    }
}
