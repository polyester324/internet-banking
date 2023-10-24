package com.tms.controller;

import com.tms.domain.Client;
import com.tms.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * ClientController is a class controller that responds to incoming requests from the path("/client")
 */
@Slf4j
@RestController
@RequestMapping("/client")
public class ClientController {
    public final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * getAll is a GET method that shows all clients from db
     * @return 200 ok
     */
    @GetMapping()
    public ResponseEntity<List<Client>> getAll(){
        log.info("getAll method working!");
        List<Client> resultList = clientService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    /**
     * getClientById is a GET method that shows the client by requested id in url path
     * @return 200 ok if client was found and 409 conflict otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable("id") Long id){
        Optional<Client> client = clientService.getClientById(id);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    /**
     * createClient is a POST method that creates the client by given json data
     * @return 201 created if client was created and 409 conflict otherwise
     */
    @PostMapping
    public ResponseEntity<HttpStatus> createClient(@RequestBody Client client){
        return new ResponseEntity<>(clientService.createClient(client) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * updateClient is a PUT method that updates the client by given json data
     * @return 204 no content if client was updated and 409 conflict otherwise
     */
    @PutMapping
    public ResponseEntity<HttpStatus> updateClient(@RequestBody Client client){
        return new ResponseEntity<>(clientService.updateClient(client) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * updateClientFirstName is a PUT method that updates client's first name by url path
     * @return 204 no content if client's first name was updated and 409 conflict otherwise
     */
    @PutMapping("/{id}/{first-name}")
    public ResponseEntity<HttpStatus> updateClientFirstName(@PathVariable("id") Long id, @PathVariable("first-name") String name){
        return new ResponseEntity<>(clientService.updateFirstName(name, id) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * updateClientLastName is a PUT method that updates client's last name by url path
     * @return 204 no content if client's last name was updated and 409 conflict otherwise
     */
    @PutMapping("/{id}/{last-name}")
    public ResponseEntity<HttpStatus> updateClientLastName(@PathVariable("id") Long id, @PathVariable("last-name") String name){
        return new ResponseEntity<>(clientService.updateLastName(name, id) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * updateClientPhoneNumber is a PUT method that updates client's phone number by url path
     * @return 204 no content if client's phone number was updated and 409 conflict otherwise
     */
    @PutMapping("/{id}/{phone-number}")
    public ResponseEntity<HttpStatus> updateClientPhoneNumber(@PathVariable("id") Long id, @PathVariable("phone-number") String phoneNumber){
        return new ResponseEntity<>(clientService.updatePhoneNumber(phoneNumber, id) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * deleteClient is a DELETE method that deletes the client by requested id in url path
     * @return 204 no content if client was deleted and 409 conflict otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable ("id") Long id){
        return new ResponseEntity<>(clientService.deleteClientById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}