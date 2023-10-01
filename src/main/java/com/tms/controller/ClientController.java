package com.tms.controller;

import com.tms.domain.Client;
import com.tms.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {
    public final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable ("id") Long id){
        Optional<Client> client = clientService.getClientById(id);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createClient(@RequestBody Client client){
        return new ResponseEntity<>(clientService.createClient(client) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping("/first-name")
    public ResponseEntity<HttpStatus> updateClientFirstName(@RequestBody Client client){
        return new ResponseEntity<>(clientService.updateClientFirstName(client) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/last-name")
    public ResponseEntity<HttpStatus> updateClientLastName(@RequestBody Client client){
        return new ResponseEntity<>(clientService.updateClientLastName(client) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/phone-number")
    public ResponseEntity<HttpStatus> updateClientPhoneNumber(@RequestBody Client client){
        return new ResponseEntity<>(clientService.updateClientPhoneNumber(client) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}