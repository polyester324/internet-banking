package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

/**
 * ProjectCard is a class, that inherits Card
 */
@Component
@DiscriminatorValue("Project bank")
@Entity
public class ProjectCard extends Card {
    public ProjectCard() {
        super();
    }
}
