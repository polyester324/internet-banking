package com.tms.service;

import com.tms.domain.Client;
import com.tms.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository CLIENT_REPOSITORY;

    public ClientService(ClientRepository clientRepository) {
        this.CLIENT_REPOSITORY = clientRepository;
    }

    public Boolean createClient(Client client) {
        return CLIENT_REPOSITORY.createClient(client);
    }

    public Client getClientById(Long id) {
        return CLIENT_REPOSITORY.getClientById(id);
    }

    public Boolean updateClientFirstName(Client client) {
        return CLIENT_REPOSITORY.updateClientFirstName(client);
    }

    public Boolean updateClientLastName(Client client) {
        return CLIENT_REPOSITORY.updateClientLastName(client);
    }

    public Boolean updateClientPhoneNumber(Client client) {
        return CLIENT_REPOSITORY.updateClientPhoneNumber(client);
    }
}
