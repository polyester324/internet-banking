package com.tms.security;

import com.tms.security.filter.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(new AntPathRequestMatcher("/security/registration", "POST")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/security", "POST")).permitAll()

                                .requestMatchers(new AntPathRequestMatcher("/client", "GET")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/client", "POST")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/client", "PUT")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/client/first-name/{id}", "PUT")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/client/last-name/{id}", "PUT")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/client/email/{id}", "PUT")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/client/password/{id}", "PUT")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/client/{id}", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/client/{id}", "DELETE")).hasAnyRole("ADMIN", "MODERATOR")

                                .requestMatchers(new AntPathRequestMatcher("/bank", "GET")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/bank/{id}", "GET")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/bank", "PUT")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/bank/{id}", "DELETE")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/bank/create-card/{id}", "POST")).permitAll()

                                .requestMatchers(new AntPathRequestMatcher("/investment", "GET")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/investment/{id}", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/investment", "POST")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/investment/{id}", "POST")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/investment", "PUT")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/investment/{id}", "DELETE")).hasRole("ADMIN")

                                .requestMatchers(new AntPathRequestMatcher("/card", "GET")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/card/{id}", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/card", "POST")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/card", "PUT")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/card/{id}", "DELETE")).hasRole("ADMIN")

                                .requestMatchers(new AntPathRequestMatcher("/transaction/transfer", "PUT")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/transaction/deposit", "PUT")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/transaction/withdraw", "PUT")).hasRole("ADMIN")

                                .requestMatchers(new AntPathRequestMatcher("/file/checks/{user}/{filename}", "GET")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/file/upload/checks/{user}", "POST")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/file/checks/{user}", "GET")).hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(new AntPathRequestMatcher("/file/checks/{user}/{filename}", "DELETE")).hasAnyRole("ADMIN", "MODERATOR")

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}