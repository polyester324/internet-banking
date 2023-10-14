package com.tms.repository;

import com.tms.domain.Card;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Slf4j
@Getter
@Repository
public class CardRepository {
    public final Session session;

    @Autowired
    public CardRepository(Session session) {
        this.session = session;
    }

    public Boolean createCard(Card card){
        try {
            session.getTransaction().begin();
            session.persist(card);
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with creation card " + card + ". The ex " + e);
        }
        return false;
    }

    public Optional<Card> getCardById(Long id){
        try {
            return Optional.ofNullable(session.get(Card.class, id));
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with getting card by id " + id + ". The ex " + e);
        }
        return Optional.empty();
    }

    public Boolean deleteCardById(Long id){
        try {
            session.getTransaction().begin();
            session.remove(getCardById(id).get());
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with deleting card by id " + id + ". The ex " + e);
        }
        return false;
    }
}
