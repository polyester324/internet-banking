package com.tms.controller;

import com.tms.domain.card.Card;
import com.tms.dtos.CardRegistrationDTO;
import com.tms.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CardController is a class controller that responds to incoming requests from the path("/card")
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {
    public final CardService cardService;

    /**
     * getClientById is a GET method that shows the card by requested id in url path
     * @return 200 ok if card was found and 409 conflict otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable ("id") Long id){
        return new ResponseEntity<>(cardService.getCardById(id), HttpStatus.OK);
    }

    /**
     * registerCard is a POST method that creates the card by given json data
     * @return 201 created if card was registered and 409 conflict otherwise
     */
    @PostMapping
    public ResponseEntity<HttpStatus> registerCard(@RequestBody CardRegistrationDTO dto){
        return new ResponseEntity<>(cardService.registerCard(dto.getCardNumber(), dto.getClientId(), dto.getBalance(), dto.getCardType(), dto.getMoneyCurrency()) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * deleteCard is a DELETE method that deletes the card by requested id in url path
     * @return 204 no content if card was deleted and 409 conflict otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable ("id") Long id){
        return new ResponseEntity<>(cardService.deleteCardById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
