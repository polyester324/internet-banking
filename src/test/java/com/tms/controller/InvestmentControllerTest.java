package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.domain.Investment;
import com.tms.domain.bank.Bank;
import com.tms.domain.bank.ProjectBank;
import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import com.tms.dtos.InvestmentCreationDTO;
import com.tms.security.filter.JwtAuthenticationFilter;
import com.tms.security.service.SecurityService;
import com.tms.service.InvestmentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = InvestmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class InvestmentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtAuthenticationFilter jaf;
    @MockBean
    InvestmentService investmentService;
    @MockBean
    SecurityService securityService;

    static List<Investment> investmentList = null;
    static Investment investment = null;
    static Card card = null;
    static Bank bank = null;

    @BeforeAll
    static void beforeAll() {
        investmentList = new ArrayList<>();
        investment = new Investment();
        investment.setId(41L);
        investment.setInvestmentNumber("000000000");
        investment.setCardId(29L);
        investment.setBankId(1L);
        investment.setMoneyCurrency("USD");
        investment.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        investment.setExpired(Timestamp.valueOf(LocalDateTime.now()));
        investment.setInvestedAmount(BigDecimal.valueOf(0));
        investment.setExpectedAmount(BigDecimal.valueOf(0));
        investment.setTime("ONE_YEAR");
        investment.setClientId(56L);
        investmentList.add(investment);

        card = new ProjectCard();
        card.setCardNumber("0000-0000-0000-0000");
        card.setBalance(BigDecimal.valueOf(100));

        bank = new ProjectBank();
        bank.setBankName("Project bank");
    }

    @Test
    void getAllTest_shouldBeOk() throws Exception {
        Mockito.when(investmentService.getAll()).thenReturn(investmentList);
        mockMvc.perform(get("/investment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(41)));
    }

    @Test
    void getInvestmentByIdTest_shouldBeOk() throws Exception{
        Mockito.when(securityService.checkAccessById(investment.getId())).thenReturn(true);
        Mockito.when(investmentService.getInvestmentById(investment.getId())).thenReturn(Optional.of(investment));
        mockMvc.perform(get("/investment/{id}", investment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(41)));
    }

    @Test
    void createInvestmentTest_shouldBeCreated() throws Exception{
        Mockito.when(investmentService.createInvestment(any())).thenReturn(true);

        mockMvc.perform(post("/investment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(investment)))
                        .andExpect(status().isCreated());
    }

    @Test
    void makeAnInvestmentTest_shouldBeCreated() throws Exception{
        Mockito.when(investmentService.updateInvestment(any())).thenReturn(true);
        InvestmentCreationDTO investmentCreationDTO = new InvestmentCreationDTO();
        investmentCreationDTO.setCardNumber(card.getCardNumber());
        investmentCreationDTO.setBankName(bank.getBankName());
        investmentCreationDTO.setMoneyCurrency(investment.getMoneyCurrency());
        investmentCreationDTO.setTime(investment.getTime());
        investmentCreationDTO.setAmount(investment.getInvestedAmount());
        Mockito.when(investmentService.makeAnInvestment(investmentCreationDTO.getCardNumber(), investmentCreationDTO.getBankName(), investmentCreationDTO.getMoneyCurrency(), investmentCreationDTO.getTime(), investmentCreationDTO.getAmount(), investment.getClientId())).thenReturn(true);

        mockMvc.perform(post("/investment/{id}", investment.getClientId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(investmentCreationDTO)))
                        .andExpect(status().isCreated());
    }

    @Test
    void updateInvestmentTest_shouldBeNoContent() throws Exception{
        Mockito.when(investmentService.updateInvestment(any())).thenReturn(true);

        mockMvc.perform(put("/investment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(investment)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteInvestmentTest_shouldBeNoContent() throws Exception{
        Mockito.when(investmentService.deleteInvestmentById(any())).thenReturn(true);
        mockMvc.perform(delete("/investment/56"))
                .andExpect(status().isNoContent());
    }
}