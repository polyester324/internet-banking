package com.tms.service;

import com.tms.domain.Client;
import com.tms.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import com.tms.security.service.SecurityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    SecurityService securityService;


    static List<Client> clientList = null;
    static Client client = null;

    static Long clientId = 56L;



    @BeforeAll
    static void beforeAll() {
        clientList = new ArrayList<>();
        client = new Client();
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        clientList.add(client);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getClientByIdTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Mockito.when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientById(clientId);
        Mockito.verify(clientRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.orElseThrow());
    }

    @Test
    void getAllTest() {
        Mockito.when(clientRepository.findAll()).thenReturn(clientList);

        List<Client> resultList = clientService.getAll();
        Mockito.verify(clientRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void createClientTest() {
        Mockito.when(clientRepository.save(any())).thenReturn(client);

        Boolean result = clientService.createClient(client);
        Mockito.verify(clientRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }

    @Test
    void updateClientTest() {
        Mockito.when(clientRepository.saveAndFlush(any())).thenReturn(client);

        Boolean result = clientService.updateClient(client);
        Mockito.verify(clientRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
    }

    @Test
    void deleteClientTest() {
        clientService.deleteClientById(clientId);
        Mockito.verify(clientRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    void updateFirstNameTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Boolean result = clientService.updateFirstName("NEW_TEST_NAME", clientId);
        Mockito.verify(clientRepository, Mockito.times(1)).updateFirstNameById(any(), any());
        Assertions.assertTrue(result);
    }

    @Test
    void updateLastNameTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Boolean result = clientService.updateLastName("NEW_TEST_NAME", clientId);
        Mockito.verify(clientRepository, Mockito.times(1)).updateLastNameById(any(), any());
        Assertions.assertTrue(result);
    }

    @Test
    void updateEmailTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Boolean result = clientService.updateEmail("TestTestTestTest@gmail.com", clientId);
        Mockito.verify(clientRepository, Mockito.times(1)).updateEmailById(any(), any());
        Assertions.assertTrue(result);
    }

    @Test
    void updatePasswordTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Boolean result = clientService.updatePassword("abCD1234", clientId);
        Mockito.verify(clientRepository, Mockito.times(1)).updatePasswordById(any(), any());
        Assertions.assertTrue(result);
    }
}