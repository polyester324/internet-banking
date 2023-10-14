package com.tms.repository;

import com.tms.domain.Client;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Slf4j
@Getter
@Repository
public class ClientRepository {
    public final Session session;

    @Autowired
    public ClientRepository(Session session) {
        this.session = session;
    }

    public Boolean createClient(Client client){
        try {
            session.getTransaction().begin();
            session.persist(client);
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with creation client " + client + ". The ex " + e);
        }
        return false;
    }

    public Optional<Client> getClientById(Long id){
        try {
            return Optional.ofNullable(session.get(Client.class, id));
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with getting client by id " + id + ". The ex " + e);
        }
        return Optional.empty();
    }

    public Boolean updateClient(Client client){
        try {
            session.getTransaction().begin();
            session.merge(client);
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with updating client " + client + ". The ex " + e);
        }
        return false;
    }

    public Boolean updateClientFirstName(Client client){
        try {
            Query<Client> query = session.createQuery("update clients set firstName=: firstName where id=: id");
            query.setParameter("firstName", client.getFirstName());
            query.setParameter("id", client.getId());
            session.getTransaction().begin();
            query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with updating client's first name " + client.getFirstName() + ". The ex " + e);
        }
        return false;
    }

    public Boolean updateClientLastName(Client client){
        try {
            Query<Client> query = session.createQuery("update clients set lastName=: lastName where id=: id");
            query.setParameter("lastName", client.getLastName());
            query.setParameter("id", client.getId());
            session.getTransaction().begin();
            query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with updating client's last name " + client.getLastName() + ". The ex " + e);
        }
        return false;
    }

    public Boolean updateClientPhoneNumber(Client client){
        try {
            Query<Client> query = session.createQuery("update clients set phoneNumber=: phoneNumber where id=: id");
            query.setParameter("phoneNumber", client.getPhoneNumber());
            query.setParameter("id", client.getId());
            session.getTransaction().begin();
            query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with updating client's phone number " + client.getPhoneNumber() + ". The ex " + e);
        }
        return false;
    }

    public Boolean deleteClientById(Long id){
        try {
            session.getTransaction().begin();
            session.remove(getClientById(id).get());
            session.getTransaction().commit();
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with getting client by id " + id + ". The ex " + e);
        }
        return false;
    }
}
