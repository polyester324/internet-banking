package com.tms.controller;

import com.tms.domain.Client;
import com.tms.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/client")
public class ClientController {
    public final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public ResponseEntity<List<Client>> getAll(){
        log.info("getAll method working!");
        List<Client> resultList = clientService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable("id") Long id){
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

    @PutMapping("/{id}/{first-name}")
    public ResponseEntity<HttpStatus> updateClientFirstName(@PathVariable("id") Long id, String name){
        return new ResponseEntity<>(clientService.updateFirstName(name, id) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}/{last-name}")
    public ResponseEntity<HttpStatus> updateClientLastName(@PathVariable("id") Long id, String name){
        return new ResponseEntity<>(clientService.updateLastName(name, id) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}/{phone-number}")
    public ResponseEntity<HttpStatus> updateClientPhoneNumber(@PathVariable("id") Long id, String phoneNumber){
        return new ResponseEntity<>(clientService.updatePhoneNumber(phoneNumber, id) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable ("id") Long id){
        return new ResponseEntity<>(clientService.deleteClientById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}