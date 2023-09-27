package com.tms.controller;

import com.tms.domain.Client;
import com.tms.service.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")

public class ClientController {
    public final ClientService CLIENT_SERVICE;

    public ClientController(ClientService clientService) {
        this.CLIENT_SERVICE = clientService;
    }

    @GetMapping
    public Client getClientById(@RequestParam Long id){
        return CLIENT_SERVICE.getClientById(id);
    }

    @PostMapping
    public Boolean createClient(@RequestBody Client client){
        return CLIENT_SERVICE.createClient(client);
    }

    @PutMapping("/first-name")
    public Boolean updateClientFirstName(@RequestBody Client client){
        return CLIENT_SERVICE.updateClientFirstName(client);
    }

    @PutMapping("/last-name")
    public Boolean updateClientLastName(@RequestBody Client client){
        return CLIENT_SERVICE.updateClientLastName(client);
    }

    @PutMapping("/phone-number")
    public Boolean updateClientPhoneNumber(@RequestBody Client client){
        return CLIENT_SERVICE.updateClientPhoneNumber(client);
    }
}
