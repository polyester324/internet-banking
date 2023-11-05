package com.tms.controller;

import com.tms.dtos.InvestmentCreationDTO;
import com.tms.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InvestmentController is a class controller that responds to incoming requests from the path("/investment")
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/investment")
public class InvestmentController {
    private final InvestmentService investmentService;

    @PostMapping
    public ResponseEntity<HttpStatus> createInvestment(@RequestBody InvestmentCreationDTO dto){
        return new ResponseEntity<>(investmentService.createInvestment(dto.getCardNumber(), dto.getBankName(), dto.getMoneyCurrency(), dto.getTime(), dto.getAmount()) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
