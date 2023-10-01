package com.tms.controller;

import com.tms.domain.Client;
import com.tms.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {
    public final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public Client getClientById(@RequestParam Long id){
        return clientService.getClientById(id);
    }

    @PostMapping
    public Boolean createClient(@RequestBody Client client){
        return clientService.createClient(client);
    }

    @PutMapping("/first-name")
    public Boolean updateClientFirstName(@RequestBody Client client){
        return clientService.updateClientFirstName(client);
    }

    @PutMapping("/last-name")
    public Boolean updateClientLastName(@RequestBody Client client){
        return clientService.updateClientLastName(client);
    }

    @PutMapping("/phone-number")
    public Boolean updateClientPhoneNumber(@RequestBody Client client){
        return clientService.updateClientPhoneNumber(client);
    }
}
