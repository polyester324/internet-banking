package com.tms.controller;

import com.tms.domain.Card;
import com.tms.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.Optional;

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
        Optional<Card> card = cardService.getCardById(id);
        return card.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    /**
     * createCard is a POST method that creates the card by given json data
     * @return 201 created if card was created and 409 conflict otherwise
     */
    @PostMapping
    public ResponseEntity<HttpStatus> createCard(@RequestBody Card card){
        return new ResponseEntity<>(cardService.createCard(card) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
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
