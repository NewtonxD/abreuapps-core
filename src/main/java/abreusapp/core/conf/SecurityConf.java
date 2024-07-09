package abreusapp.core.conf;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConf{
        
    @Bean
    public AuthenticationSuccessHandler myAuthSuccessHandler(){
        return new AuthSuccessHandler();
    }
    
    private final SpecificRLFilter RL1Filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilterBefore(RL1Filter, LogoutFilter.class)
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
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
            //CorsConfiguration configuration = new CorsConfiguration();
            //configuration.setAllowedOrigins(Arrays.asList("/**"));
            //configuration.setAllowedMethods(Arrays.asList("GET","POST"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
            return source;
    }
    
    
}
