package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

/**
 * AlphaCard is a class, that inherits Card
 */
@Component
@DiscriminatorValue("Alpha bank")
@Entity
public class AlphaCard extends Card {
    public AlphaCard() {
        super();
    }
}
