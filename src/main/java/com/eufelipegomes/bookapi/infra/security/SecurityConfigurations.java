package com.eufelipegomes.bookapi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
  @Autowired
  private SecurityFilter securityFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csfr -> csfr.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "/users/").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/users/").permitAll()
            .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole(
                "USER")
            .requestMatchers(HttpMethod.GET, "/books/user/{uid}").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/books/").permitAll()
            .requestMatchers(HttpMethod.POST, "/books/user/{uid}").hasRole("USER")
            .requestMatchers(HttpMethod.PUT, "/books/book/{uid}").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/books/book/{uid}").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/books/notes/book/{bookid}").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/books/notes/user/{userId}/book/{bookId}").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/user/collections/{uid}").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/user/collections/{uid}").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/user/collections/collection/{collectionId}/book/{bookId}")
            .hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/user/collections/collection/{collectionId}/books").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/user/collections/collection/{collectionId}/books/{bookId}")
            .hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/user/collections/collection/{collectionId}").hasRole("USER")
            .anyRequest().authenticated())

        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();

  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
