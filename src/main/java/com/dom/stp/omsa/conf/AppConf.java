package com.dom.stp.omsa.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import com.dom.stp.omsa.control.domain.usuario.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@Configuration
@RequiredArgsConstructor
public class AppConf {
    
    @Autowired
    UsuarioRepo UsuarioRepositorio;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> UsuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe!"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomAuthProv authProvider = new CustomAuthProv(userDetailsService(), passwordEncoder());
        return authProvider;
    }
 
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() { 
        return new SessionRegistryImpl(); 
    }
    
}
