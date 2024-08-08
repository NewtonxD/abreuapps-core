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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConf{
        
    @Bean
    public AuthenticationSuccessHandler myAuthSuccessHandler(){
        return new AuthSuccessHandler();
    }
    
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
                .successHandler(myAuthSuccessHandler())
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
