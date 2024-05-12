package abreusapp.core.conf;

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
import abreusapp.core.control.usuario.UsuarioRepo;
import abreusapp.core.control.utils.NotificationHandler;
import abreusapp.core.control.utils.NotifierServ;
import com.zaxxer.hikari.util.DriverDataSource;
import java.util.Properties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@Configuration
@RequiredArgsConstructor
public class AppConf {

    private final UsuarioRepo UsuarioRepositorio;
    
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
    
    @Bean
    NotifierServ notifier(DataSourceProperties props) {
        
        DriverDataSource ds = new DriverDataSource(
           props.determineUrl(), 
           props.determineDriverClassName(),
           new Properties(), 
           props.determineUsername(),
           props.determinePassword());
        
        JdbcTemplate tpl = new JdbcTemplate(ds);

        return new NotifierServ(tpl);
    }
    
    @Bean
    CommandLineRunner startListener(NotifierServ notifier, NotificationHandler handler) {
        return (args) -> {         
            Runnable listener = notifier.createNotificationHandler(handler);            
            Thread t = new Thread(listener, "DBNotification-listener");
            t.start();
        };
    }
    
    
    
}
