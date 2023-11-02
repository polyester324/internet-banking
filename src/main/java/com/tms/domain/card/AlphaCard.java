package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@DiscriminatorValue("Alpha bank")
@Entity
public class AlphaCard extends Card {
    public AlphaCard() {
        super();
    }
}
