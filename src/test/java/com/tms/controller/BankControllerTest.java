package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.domain.bank.Bank;
import com.tms.domain.bank.ProjectBank;
import com.tms.security.filter.JwtAuthenticationFilter;
import com.tms.security.service.SecurityService;
import com.tms.service.BankService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BankController.class)
@AutoConfigureMockMvc(addFilters = false)
class BankControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtAuthenticationFilter jaf;
    @MockBean
    BankService bankService;
    @MockBean
    SecurityService securityService;

    static List<Bank> bankList = null;
    static Bank bank = null;

    @BeforeAll
    static void beforeAll() {
        bankList = new ArrayList<>();
        bank = new ProjectBank();
        bank.setId(1L);
        bank.setBankName("Project bank");
        bank.setCommission(BigDecimal.valueOf(0.1));
        bank.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        bankList.add(bank);
    }

    @Test
    void getAllTest_shouldBeOk() throws Exception {
        Mockito.when(bankService.getAll()).thenReturn(bankList);
        mockMvc.perform(get("/bank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)));
    }

    @Test
    void getBankByIdTest_shouldBeOk() throws Exception{
        Mockito.when(securityService.checkAccessById(bank.getId())).thenReturn(true);
        Mockito.when(bankService.getBankById(bank.getId())).thenReturn(bank);
        mockMvc.perform(get("/bank/{id}", bank.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)));
    }

    @Test
    void deleteBankTest_shouldBeNoContent() throws Exception{
        Mockito.when(bankService.deleteBankById(any())).thenReturn(true);
        mockMvc.perform(delete("/bank/{id}", bank.getId()))
                .andExpect(status().isNoContent());
    }
}