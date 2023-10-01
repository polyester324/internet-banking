package com.tms.service;

import com.tms.domain.Client;
import com.tms.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Boolean createClient(Client client) {
        return clientRepository.createClient(client);
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.getClientById(id);
    }

    public Boolean updateClientFirstName(Client client) {
        return clientRepository.updateClientFirstName(client);
    }

    public Boolean updateClientLastName(Client client) {
        return clientRepository.updateClientLastName(client);
    }

    public Boolean updateClientPhoneNumber(Client client) {
        return clientRepository.updateClientPhoneNumber(client);
    }
}
