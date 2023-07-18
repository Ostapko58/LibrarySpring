package com.onyshkiv.libraryspring.config;

import com.onyshkiv.libraryspring.security.AuthProviderImpl;
import com.onyshkiv.libraryspring.service.MyUserDetailsService;
import com.onyshkiv.libraryspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final MyUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET,"/books").hasAnyRole("ADMINISTRATOR", "USER", "LIBRARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/activeBooks/return/{id}", "/activeBooks/{id}"
                                , "/authors/{id}", "/books/{isbn}", "/publications/{id}", "/users/{login}", "/users/status/{login}").hasAnyRole("LIBRARIAN", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/users/status/{login}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/authors", "/books", "/publications", "/users/librarian").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/activeBooks/{id}").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/activeBooks/{id}", "/authors/{id}", "/publications/{id}", "/users/{login}", "/books/{isbn}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        //.anyRequest().hasAnyRole("ADMINISTRATOR", "USER", "LIBRARIAN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
