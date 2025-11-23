package com.springboot.backend.sidrick.userapp.users_backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.springboot.backend.sidrick.userapp.users_backend.auth.filter.JwtAuthenticationFilter;
import com.springboot.backend.sidrick.userapp.users_backend.auth.filter.JwtValidationFilter;

import java.util.Arrays;

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
        .cors(cors -> cors.configurationSource(ourCustomConfigurationSource()))
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();

    }

    @Bean
    CorsConfigurationSource ourCustomConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<CorsFilter>(
            new CorsFilter(this.ourCustomConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }

}
