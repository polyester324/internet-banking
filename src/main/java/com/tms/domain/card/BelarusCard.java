package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@DiscriminatorValue("Belarus bank")
@Entity
public class BelarusCard extends Card {
    public BelarusCard() {
        super();
    }
}
