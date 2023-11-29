package com.tms.controller;

import com.tms.domain.Investment;
import com.tms.dtos.InvestmentCreationDTO;
import com.tms.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

/**
 * InvestmentController is a class controller that responds to incoming requests from the path("/investment")
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/investment")
public class InvestmentController {
    private final InvestmentService investmentService;

    /**
     * getAll is a GET method that shows all investments from db
     * @return 200 ok
     */
    @GetMapping()
    public ResponseEntity<List<Investment>> getAll(){
        log.info("Investment getAll method working!");
        List<Investment> resultList = investmentService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    /**
     * getInvestmentById is a GET method that shows the investment by requested id in url path
     * @return 200 ok if investment was found and 409 conflict otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Investment> getInvestmentById(@PathVariable("id") Long id){
        Optional<Investment> investment = investmentService.getInvestmentById(id);
        return investment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    /**
     * createInvestment is a POST method that creates the investment by given json data
     * @return 201 created if investment was created and 409 conflict otherwise
     */
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> createInvestment(@PathVariable("id") Long id, @RequestBody InvestmentCreationDTO dto){
        return new ResponseEntity<>(investmentService.createInvestment(dto.getCardNumber(), dto.getBankName(), dto.getMoneyCurrency(), dto.getTime(), dto.getAmount(), id) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    /**
     * updateInvestment is a PUT method that updates the investment by given json data
     * @return 204 no content if investment was updated and 409 conflict otherwise
     */
    @PutMapping
    public ResponseEntity<HttpStatus> updateInvestment(@RequestBody Investment investment){
        return new ResponseEntity<>(investmentService.updateInvestment(investment) ?  HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    /**
     * deleteInvestment is a DELETE method that deletes the investment by requested id in url path
     * @return 204 no content if investment was deleted and 409 conflict otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteInvestment(@PathVariable ("id") Long id){
        return new ResponseEntity<>(investmentService.deleteInvestmentById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
