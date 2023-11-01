package com.tms.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
@DiscriminatorValue("Project Card")
@Entity
public class ProjectCard extends Card {
    public ProjectCard() {
        super();
    }
}
