package com.springboot.backend.sidrick.userapp.users_backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.backend.sidrick.userapp.users_backend.auth.filter.JwtAuthenticationFilter;
import com.springboot.backend.sidrick.userapp.users_backend.auth.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //We annotate it as a Bean so that it gets registered in the Spring Container as a Component.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //The first line means that the routes /api/users and /api/users/page/{page} are accessible to anyone, regardless of whether they're logged in or not.
        //The second requestMatchers() line means that a user details by ID can be viewed by logged users of any type.
        //The subsequent requestMatchers() lines mean that POST, PUT and DELETE requests can be issued only by logged in ADMIN users.
        return http.authorizeHttpRequests( authz -> authz
        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/page/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
        .anyRequest().authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();

    }

}
