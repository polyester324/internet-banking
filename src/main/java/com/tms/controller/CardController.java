package com.tms.controller;

import com.tms.domain.card.Card;
import com.tms.dtos.CardRegistrationDTO;
import com.tms.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

/**
 * CardController is a class controller that responds to incoming requests from the path("/card")
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {
    public final CardService cardService;

    /**
     * getAll is a GET method that shows all clients from db
     * @return 200 ok
     */
    @GetMapping()
    public ResponseEntity<List<Card>> getAll(){
        log.info("Card getAll method working!");
        List<Card> resultList = cardService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    /**
     * getCardById is a GET method that shows the card by requested id in url path
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
    public ResponseEntity<HttpStatus> registerCard(@Valid @RequestBody CardRegistrationDTO dto){
        return new ResponseEntity<>(cardService.registerCard(dto.getCardNumber(), dto.getClientId(), dto.getBalance(), dto.getCardType(), dto.getMoneyCurrency()) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * updateCard is a PUT method that updates the card by given json data
     * @return 204 no content if card was updated and 409 conflict otherwise
     */
    @PutMapping
    public ResponseEntity<HttpStatus> updateCard(@RequestBody Card card){
        return new ResponseEntity<>(cardService.updateCard(card) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
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
