package com.tms.domain.bank;

import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Project bank")
@Data
@Entity
public class ProjectBank extends Bank implements BankFactory {
    public ProjectBank() {
        super();
    }

    @Override
    public Card createCard() {
        return new ProjectCard();
    }
}
