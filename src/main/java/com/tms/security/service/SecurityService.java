package com.tms.security.service;

import com.tms.domain.Client;
import com.tms.domain.Role;
import com.tms.exceptions.SameClientInDatabaseException;
import com.tms.repository.ClientRepository;
import com.tms.security.domain.SecurityCredentials;
import com.tms.security.domain.dto.AuthRequest;
import com.tms.security.domain.dto.RegistrationDTO;
import com.tms.security.repository.SecurityCredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SecurityService {

    private final SecurityCredentialsRepository securityCredentialsRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public Optional<String> generateToken(AuthRequest authRequest) {
        Optional<SecurityCredentials> personCredentials =
                securityCredentialsRepository.getByEmail(authRequest.getEmail());
        if (personCredentials.isPresent() &&
                passwordEncoder.matches(authRequest.getPassword(), personCredentials.get().getPassword())) {
            return Optional.of(jwtUtils.generateJwtToken(authRequest.getEmail()));
        }
        return Optional.empty();
    }

    @Transactional(rollbackOn = Exception.class)
    public void registration(RegistrationDTO registrationDTO) {
        Optional<SecurityCredentials> result = securityCredentialsRepository.getByEmail(registrationDTO.getEmail());
        if (result.isPresent()) {
            throw new SameClientInDatabaseException();
        }
        Client client = new Client();
        client.setFirstName(registrationDTO.getFirstName());
        client.setLastName(registrationDTO.getLastName());
        client.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        Client clientInfoResult = clientRepository.save(client);

        SecurityCredentials securityCredentials = new SecurityCredentials();
        securityCredentials.setEmail(registrationDTO.getEmail());
        securityCredentials.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        securityCredentials.setRole(Role.USER);
        securityCredentials.setClient_id(clientInfoResult.getId());
        securityCredentialsRepository.save(securityCredentials);
    }

    public boolean checkAccessById(Long id) {
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        String userRole = String.valueOf(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get());
        Long userId = securityCredentialsRepository.findUserIdByLogin(userLogin);
        return (userId.equals(id) || userRole.equals("ROLE_ADMIN"));
    }
}