package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.domain.card.Card;
import com.tms.domain.card.ProjectCard;
import com.tms.dtos.CardRegistrationDTO;
import com.tms.security.filter.JwtAuthenticationFilter;
import com.tms.security.service.SecurityService;
import com.tms.service.CardService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtAuthenticationFilter jaf;
    @MockBean
    CardService cardService;
    @MockBean
    SecurityService securityService;

    static List<Card> cardList = null;
    static Card card = null;

    @BeforeAll
    static void beforeAll() {
        cardList = new ArrayList<>();
        card = new ProjectCard();
        card.setId(29L);
        card.setCardNumber("1111-1111-1111-1111");
        card.setClientId(56L);
        card.setBalance(BigDecimal.valueOf(10000));
        card.setMoneyCurrency("USD");
        card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        card.setCardType("Project bank");
        cardList.add(card);
    }

    @Test
    void getAllTest_shouldBeOk() throws Exception {
        Mockito.when(cardService.getAll()).thenReturn(cardList);
        mockMvc.perform(get("/card"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(29)));
    }

    @Test
    void getCardByIdTest_shouldBeOk() throws Exception{
        Mockito.when(securityService.checkAccessById(card.getId())).thenReturn(true);
        Mockito.when(cardService.getCardById(card.getId())).thenReturn(card);
        mockMvc.perform(get("/card/{id}", card.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(29)));
    }

    @Test
    void createCardTest_shouldBeCreated() throws Exception{
        CardRegistrationDTO cardRegistrationDTO = new CardRegistrationDTO();
        cardRegistrationDTO.setCardNumber(card.getCardNumber());
        cardRegistrationDTO.setClientId(card.getClientId());
        cardRegistrationDTO.setBalance(card.getBalance());
        cardRegistrationDTO.setMoneyCurrency(card.getMoneyCurrency());
        cardRegistrationDTO.setCardType(card.getCardType());
        Mockito.when(cardService.createCard(cardRegistrationDTO.getCardNumber(), cardRegistrationDTO.getClientId(), cardRegistrationDTO.getBalance(), cardRegistrationDTO.getMoneyCurrency(), cardRegistrationDTO.getCardType())).thenReturn(true);

        mockMvc.perform(post("/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardRegistrationDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCardTest_shouldBeNoContent() throws Exception{
        CardRegistrationDTO cardRegistrationDTO = new CardRegistrationDTO();
        cardRegistrationDTO.setId(card.getId());
        cardRegistrationDTO.setCardNumber(card.getCardNumber());
        cardRegistrationDTO.setClientId(card.getClientId());
        cardRegistrationDTO.setBalance(card.getBalance());
        cardRegistrationDTO.setMoneyCurrency(card.getMoneyCurrency());
        cardRegistrationDTO.setCardType(card.getCardType());
        Mockito.when(cardService.updateCard(cardRegistrationDTO.getId(), cardRegistrationDTO.getCardNumber(), cardRegistrationDTO.getClientId(), cardRegistrationDTO.getBalance(), cardRegistrationDTO.getMoneyCurrency(), cardRegistrationDTO.getCardType())).thenReturn(true);

        mockMvc.perform(put("/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardRegistrationDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCardTest_shouldBeNoContent() throws Exception{
        Mockito.when(cardService.deleteCardById(any())).thenReturn(true);
        mockMvc.perform(delete("/card/{id}", card.getId()))
                .andExpect(status().isNoContent());
    }
}