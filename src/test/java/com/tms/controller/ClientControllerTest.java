package com.tms.controller;

import com.tms.domain.Client;
import com.tms.dtos.ClientEmailDTO;
import com.tms.dtos.ClientFirstNameDTO;
import com.tms.dtos.ClientLastNameDTO;
import com.tms.dtos.ClientPasswordDTO;
import com.tms.security.service.SecurityService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.security.filter.JwtAuthenticationFilter;
import com.tms.service.ClientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtAuthenticationFilter jaf;
    @MockBean
    ClientService clientService;
    @MockBean
    SecurityService securityService;

    static List<Client> clientList = null;
    static Client client = null;

    @BeforeAll
    static void beforeAll() {
        clientList = new ArrayList<>();
        client = new Client();
        client.setId(56L);
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        clientList.add(client);
    }

    @Test
    void getAllTest_shouldBeOk() throws Exception {
        Mockito.when(clientService.getAll()).thenReturn(clientList);
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(56)));
    }

    @Test
    void getClientByIdTest_shouldBeOk() throws Exception{
        Mockito.when(securityService.checkAccessById(client.getId())).thenReturn(true);
        Mockito.when(clientService.getClientById(client.getId())).thenReturn(Optional.of(client));
        mockMvc.perform(get("/client/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(56)));
    }

    @Test
    void createClientTest_shouldBeCreated() throws Exception{
        Mockito.when(clientService.createClient(any())).thenReturn(true);

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                        .andExpect(status().isCreated());
    }

    @Test
    void updateClientTest_shouldBeNoContent() throws Exception{
        Mockito.when(clientService.updateClient(any())).thenReturn(true);

        mockMvc.perform(put("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                        .andExpect(status().isNoContent());
    }

    @Test
    void updateClientFirstNameTest_shouldBeNoContent() throws Exception {
        ClientFirstNameDTO dtoFirstName = new ClientFirstNameDTO();
        dtoFirstName.setFirstName("tester");
        Mockito.when(securityService.checkAccessById(client.getId())).thenReturn(true);
        Mockito.when(clientService.updateFirstName(Mockito.any(),Mockito.anyLong())).thenReturn(true);
        mockMvc.perform(put("/client/first-name/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoFirstName)))
                        .andExpect(status().isNoContent());
    }
    @Test
    void updateClientLastNameTest_shouldBeNoContent() throws Exception {
        ClientLastNameDTO dtoLastName = new ClientLastNameDTO();
        dtoLastName.setLastName("Tester");
        Mockito.when(securityService.checkAccessById(client.getId())).thenReturn(true);
        Mockito.when(clientService.updateLastName(Mockito.any(),Mockito.anyLong())).thenReturn(true);
        mockMvc.perform(put("/client/last-name/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoLastName)))
                        .andExpect(status().isNoContent());
    }

    @Test
    void updateClientEmailTest_shouldBeNoContent() throws Exception {
        ClientEmailDTO dtoEmail = new ClientEmailDTO();
        dtoEmail.setEmail("qreyfbsk@gmail.com");
        Mockito.when(securityService.checkAccessById(client.getId())).thenReturn(true);
        Mockito.when(clientService.updateEmail(Mockito.any(),Mockito.anyLong())).thenReturn(true);
        mockMvc.perform(put("/client/email/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEmail)))
                        .andExpect(status().isNoContent());
    }

    @Test
    void updateClientPasswordTest_shouldBeNoContent() throws Exception {
        ClientPasswordDTO dtoPassword = new ClientPasswordDTO();
        dtoPassword.setPassword("newPassword1234");
        Mockito.when(securityService.checkAccessById(client.getId())).thenReturn(true);
        Mockito.when(clientService.updatePassword(Mockito.any(),Mockito.anyLong())).thenReturn(true);
        mockMvc.perform(put("/client/password/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoPassword)))
                        .andExpect(status().isNoContent());
    }

    @Test
    void deleteClientTest_shouldBeNoContent() throws Exception{
        Mockito.when(clientService.deleteClientById(any())).thenReturn(true);
        mockMvc.perform(delete("/client/{id}", client.getId()))
                .andExpect(status().isNoContent());
    }
}