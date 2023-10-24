package com.tms.repository;

import com.tms.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Modifying
    @Query("update clients c set c.firstName = :firstName where c.id = :id")
    void updateFirstNameById(String firstName, Long id);

    @Modifying
    @Query("update clients c set c.lastName = :lastName where c.id = :id")
    void updateLastNameById(String lastName, Long id);

    @Modifying
    @Query("update clients c set c.phoneNumber = :phoneNumber where c.id = :id")
    void updatePhoneNumberById(String phoneNumber, Long id);
}
