package com.tms.repository;

import com.tms.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * ClientRepository is an interface, that has a connection to <i>clients table
 * performs additional operations associated with the table <i>clients
 */

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * updateFirstNameById is a method, that updates client's first name
     */
    @Modifying
    @Query("update clients c set c.firstName = :firstName where c.id = :id")
    void updateFirstNameById(String firstName, Long id);

    /**
     * updateLastNameById is a method, that updates client's last name
     */
    @Modifying
    @Query("update clients c set c.lastName = :lastName where c.id = :id")
    void updateLastNameById(String lastName, Long id);

    /**
     * updatePhoneNumberById is a method, that updates client's phone number
     */
    @Modifying
    @Query("update clients c set c.phoneNumber = :phoneNumber where c.id = :id")
    void updatePhoneNumberById(String phoneNumber, Long id);
}
