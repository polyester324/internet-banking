package com.tms.controller;

import com.tms.domain.Client;
import com.tms.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<HttpStatus> updateClient(@RequestBody Client client){
        return new ResponseEntity<>(clientService.updateClient(client) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable ("id") Long id){
        return new ResponseEntity<>(clientService.deleteClientById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}