package com.tms.service;

import com.tms.domain.Client;
import com.tms.exceptions.NoAccessByIdException;
import com.tms.repository.ClientRepository;
import com.tms.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ClientService is a class, that has a connection to <i>ClientRepository
 * performs crud operations associated with the table <i>clients
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final SecurityService securityService;

    /**
     * Method getAll shows json data of all clients in the db
     * @return List<Client>
     */
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    /**
     * Method getClientById shows json data of client with requested id
     * @return Optional<Client>
     */
    public Optional<Client> getClientById(Long id){
        if (securityService.checkAccessById(id)) {
            return clientRepository.findById(id);
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method createClient adds client from json data to db
     * @return true if client was created and false otherwise
     */
    public Boolean createClient(Client client) {
        try {
            client.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            clientRepository.save(client);
            log.info(String.format("client with first name %s was created", client.getFirstName()));
        } catch (Exception e){
            log.warn(String.format("have problem creating client with first name %s have error %s", client.getFirstName(), e));
            return false;
        }
        return true;
    }

    /**
     * Method updateClient updates client from json data to db
     * @return true if client was updated and false otherwise
     */
    public Boolean updateClient(Client client) {
        try {
            clientRepository.saveAndFlush(client);
            log.info(String.format("client with id %s was updated", client.getId()));
        } catch (Exception e){
            log.warn(String.format("have problem updating client with id %s have error %s", client.getId(), e));
            return false;
        }
        return true;
    }

    /**
     * Method updateFirstName updates client's first name from url path to db
     * @return true if client's first name was updated and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFirstName(String name, long id){
        if (securityService.checkAccessById(id)) {
            try {
                clientRepository.updateFirstNameById(name, id);
                log.info(String.format("client's first name with id %s was updated", id));
            } catch (Exception e){
                log.info(String.format("client's first name with id %s was not updated", id));
                return false;
            }
            return true;
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method updateLastName updates client's last name from url path to db
     * @return true if client's last name was updated and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateLastName(String name, long id){
        if (securityService.checkAccessById(id)) {
            try {
                clientRepository.updateLastNameById(name, id);
                log.info(String.format("client's last name with id %s was updated", id));
            } catch (Exception e){
                log.info(String.format("client's last name with id %s was not updated", id));
                return false;
            }
            return true;
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method updateEmail updates client's email from url path to db
     * @return true if client's email was updated and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEmail(String email, long id){
        if (securityService.checkAccessById(id)) {
            try {
                clientRepository.updateEmailById(email, id);
                log.info(String.format("client's email with id %s was updated", id));
            } catch (Exception e){
                log.info(String.format("client's email with id %s was not updated", id));
                return false;
            }
            return true;
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method updatePassword updates client's password from url path to db
     * @return true if client's password was updated and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(String password, long id){
        if (securityService.checkAccessById(id)) {
            try {
                clientRepository.updatePasswordById(password, id);
                log.info(String.format("client's password with id %s was updated", id));
            } catch (Exception e){
                log.info(String.format("client's password with id %s was not updated", id));
                return false;
            }
            return true;
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method deleteClientById deletes client from db by id
     * @return true if client was deleted and false otherwise
     */
    public Boolean deleteClientById(Long id) {
        try {
            clientRepository.deleteById(id);
            log.info(String.format("client with id %s was deleted", id));
        } catch (Exception e){
            log.warn(String.format("have problem deleting client with id %s have error %s", id, e));
            return false;
        }
        return true;
    }
}
