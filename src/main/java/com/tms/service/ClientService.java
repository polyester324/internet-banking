package com.tms.service;

import com.tms.domain.Client;
import com.tms.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id){
        return clientRepository.findById(id);
    }

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

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFirstName(String name, long id){
        try {
            clientRepository.updateFirstNameById(name, id);
            log.info(String.format("client's first name with id %s was updated", id));
        } catch (Exception e){
            log.info(String.format("client's first name with id %s was not updated", id));
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateLastName(String name, long id){
        try {
            clientRepository.updateLastNameById(name, id);
            log.info(String.format("client's last name with id %s was updated", id));
        } catch (Exception e){
            log.info(String.format("client's last name with id %s was not updated", id));
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePhoneNumber(String phoneNumber, long id){
        try {
            clientRepository.updatePhoneNumberById(phoneNumber, id);
            log.info(String.format("client's phone number with id %s was updated", id));
        } catch (Exception e){
            log.info(String.format("client's phone number with id %s was not updated", id));
            return false;
        }
        return true;    }

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
