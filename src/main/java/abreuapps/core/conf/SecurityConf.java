package abreuapps.core.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConf{

    private final AuthSuccessHandler successHandler;

    private final AuthFailureHandler failureHandler;

    private final SessionAuthFailureHandler SessionFailureHandler;

    private final LogoutSessionSuccessHandler logoutSuccessHandler;
    
    private final SpecificRLFilter RL1Filter;
    
    private final LocationFilter LocFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(RL1Filter, LogoutFilter.class)
            .addFilterBefore(LocFilter, SpecificRLFilter.class)
            .authorizeHttpRequests(t -> t
                .requestMatchers("/", "/auth/**", "/content/**", "/error/**","/API/**","/p/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
            )
            .sessionManagement(session -> session
                .sessionFixation()
                .migrateSession()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionAuthenticationFailureHandler(SessionFailureHandler)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/auth/login?expiredSession=true")
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(logoutSuccessHandler)
            );

        return http.build();
    }    
    
}
