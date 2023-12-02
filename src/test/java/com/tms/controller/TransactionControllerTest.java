package com.tms.controller;

import com.tms.dtos.CardTransactionDepositAndWithdrawDTO;
import com.tms.dtos.CardTransactionTransferDTO;
import com.tms.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.security.filter.JwtAuthenticationFilter;
import com.tms.security.service.SecurityService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtAuthenticationFilter jaf;
    @MockBean
    TransactionService transactionService;
    @MockBean
    SecurityService securityService;
    @MockBean
    FileController fileController;
    static Long clientId = 56L;
    static String user = "56-TESTER-TESTER";
    static String filename = "Test-File-check_43356_20231130_0000-00_1294053b-06f9-4aa6-a729-5812ccb3e072.txt";
    static ArrayList<String> list = new ArrayList<>();

    @BeforeEach
    void createFile() throws Exception {
        Path path = Paths.get("checks", user, filename);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
    }
    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get("checks", user, filename));
    }

    @Test
    void transferMoneyBetweenTwoClients() throws Exception{
        Mockito.when(securityService.checkAccessById(clientId)).thenReturn(true);
        Mockito.when(transactionService.transfer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(list);
        list.add(user);
        list.add(filename);
        Path path = Paths.get("checks", user, filename);
        Resource resource = new UrlResource(path.toUri());
        Mockito.when(fileController.getFile(user, filename)).thenReturn(new ResponseEntity<>(resource, HttpStatus.OK));
        CardTransactionTransferDTO transferDTO = new CardTransactionTransferDTO();
        mockMvc.perform(MockMvcRequestBuilders.put("/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDTO)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void putMoneyIntoTheAccount() throws Exception {
        Mockito.when(securityService.checkAccessById(clientId)).thenReturn(true);
        Mockito.when(transactionService.deposit(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(list);
        list.add(user);
        list.add(filename);
        Path path = Paths.get("checks", user, filename);
        Resource resource = new UrlResource(path.toUri());
        Mockito.when(fileController.getFile(user, filename)).thenReturn(new ResponseEntity<>(resource, HttpStatus.OK));
        CardTransactionDepositAndWithdrawDTO depositDTO = new CardTransactionDepositAndWithdrawDTO();
        mockMvc.perform(MockMvcRequestBuilders.put("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void withdrawMoneyFromTheAccount() throws Exception {
        Mockito.when(securityService.checkAccessById(clientId)).thenReturn(true);
        Mockito.when(transactionService.withdraw(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(list);
        list.add(user);
        list.add(filename);
        Path path = Paths.get("checks", user, filename);
        Resource resource = new UrlResource(path.toUri());
        Mockito.when(fileController.getFile(user, filename)).thenReturn(new ResponseEntity<>(resource, HttpStatus.OK));
        CardTransactionDepositAndWithdrawDTO withdrawDTO = new CardTransactionDepositAndWithdrawDTO();
        mockMvc.perform(MockMvcRequestBuilders.put("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}