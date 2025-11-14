package com.springboot.backend.sidrick.userapp.users_backend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    //We annotate it as a Bean so that it gets registered in the Spring Container as a Component.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //With these configurations, all routes will be protected except /api/users and /api/users/page/{page}
        return http.authorizeHttpRequests( authz -> authz
        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/page/**").permitAll()
            .anyRequest().authenticated()
        )
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();

    }

}
