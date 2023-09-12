package com.dom.stp.omsa.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http.cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/", "/auth/**", "/content/**", "/error/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(form -> form
                    .loginPage("/auth/login")
                    .defaultSuccessUrl("/main/index")
                    .failureUrl("/auth/login?error=true")
                )
                .sessionManagement(session -> session
                    .sessionFixation().migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/auth/login?logout=true")
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)
                )
                .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/auth/login?logout=true")
                );

        return http.build();
    }
}
