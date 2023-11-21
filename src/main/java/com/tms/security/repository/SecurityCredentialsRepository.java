package com.tms.security.repository;

import com.tms.security.domain.SecurityCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SecurityCredentialsRepository extends JpaRepository<SecurityCredentials, Long> {
    Optional<SecurityCredentials> getByEmail(String email);

    @Query(
            nativeQuery = true,
            value = "SELECT client_id FROM security WHERE email = ?1")
    Long findUserIdByLogin(String login);
}