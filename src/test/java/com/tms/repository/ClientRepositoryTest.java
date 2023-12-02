package com.tms.repository;

import com.tms.domain.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    static Client clientInfo;

    @BeforeAll
    static void beforeAll() {
        clientInfo = new Client();
        clientInfo.setFirstName("TestFirstName");
        clientInfo.setLastName("TestLastName");
        clientInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void findAllTest() {
        clientRepository.save(clientInfo);
        List<Client> newList = clientRepository.findAll();
        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        Client saved = clientRepository.save(clientInfo);
        Optional<Client> newClient = clientRepository.findById(saved.getId());
        Assertions.assertTrue(newClient.isPresent());
    }

    @Test
    void saveTest() {
        List<Client> oldList = clientRepository.findAll();
        clientRepository.save(clientInfo);
        List<Client> newList = clientRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        Client clientSaved = clientRepository.save(clientInfo);
        clientSaved.setFirstName("UPDATED_FIRST_NAME");
        clientSaved.setLastName("UPDATED_LAST_NAME");
        LocalDateTime time = LocalDateTime.now();
        clientSaved.setCreated(Timestamp.valueOf(time));
        Client clientUpdated = clientRepository.saveAndFlush(clientSaved);
        Assertions.assertEquals(clientUpdated.getFirstName(), "UPDATED_FIRST_NAME");
        Assertions.assertEquals(clientUpdated.getLastName(), "UPDATED_LAST_NAME");
        Assertions.assertEquals(clientUpdated.getCreated(), Timestamp.valueOf(time));
    }

    @Test
    void deleteTest() {
        Client clientSaved = clientRepository.save(clientInfo);
        clientRepository.delete(clientSaved);
        Optional<Client> client = clientRepository.findById(clientSaved.getId());
        Assertions.assertFalse(client.isPresent());
    }
}