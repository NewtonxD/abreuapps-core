package abreuapps.core.conf;

import abreuapps.core.control.utils.NotificationHandler;
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
import abreuapps.core.control.usuario.UsuarioRepo;
import abreuapps.core.control.utils.LocNotifierServ;
import abreuapps.core.control.utils.LoginAttemptServ;
import abreuapps.core.control.utils.NotifierServ;
import abreuapps.core.control.utils.SSEServ;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zaxxer.hikari.util.DriverDataSource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class AppConf {

    private final UsuarioRepo UsuarioRepositorio;
    
    private final SSEServ SSEserv;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> UsuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe!"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        AuthProv authProvider = new AuthProv(userDetailsService(), passwordEncoder(), loginAttemptServ());
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
    public LoginAttemptServ loginAttemptServ() {
        return new LoginAttemptServ();
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
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    
    @Bean
    public NotifierServ notifier(DataSourceProperties props) {
        
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
    public LocNotifierServ LocNotifier(DataSourceProperties props) {
        
        DriverDataSource ds = new DriverDataSource(
           props.determineUrl(), 
           props.determineDriverClassName(),
           new Properties(), 
           props.determineUsername(),
           props.determinePassword());
        
        JdbcTemplate tpl = new JdbcTemplate(ds);

        return new LocNotifierServ(tpl,SSEserv);
        
    }
    
    @Bean
    public CommandLineRunner startListener(NotifierServ notifier,NotificationHandler handler) {
        return (args) -> {         
            Runnable listener = notifier.createNotificationHandler(handler);            
            Thread t = new Thread(listener, "DBNotification-listener");
            t.start();
        };
    }
    
    
    @Bean
    public CommandLineRunner startListenerLoc(LocNotifierServ locNotifier) {
        return (args) -> {   
            Runnable listener = locNotifier.createNotificationHandler();            
            Thread t = new Thread(listener, "DBNotification-listener1");
            t.start();
        };
    }
    
    private CaffeineCache buildCache(String name, Duration expireAfterWriteDuration) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWriteDuration)
                .weakKeys()
                .build());
    }

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = new ArrayList<>();

        
        caches.add(buildCache("Tiles", Duration.ofHours(12)));
        caches.add(buildCache("Paradas", Duration.ofHours(8)));
        caches.add(buildCache("Rutas", Duration.ofHours(4)));
        caches.add(buildCache("RutasInfo", Duration.ofHours(12)));
        
        caches.add(buildCache("RutasLoc", Duration.ofHours(8)));
        caches.add(buildCache("Vehiculos", Duration.ofHours(4)));
        
        caches.add(buildCache("PMC", Duration.ofMinutes(30)));
        caches.add(buildCache("PI", Duration.ofSeconds(10)));
        caches.add(buildCache("LV", Duration.ofSeconds(10)));
        
        caches.add(buildCache("Usuario", Duration.ofHours(8)));
        caches.add(buildCache("Usuarios", Duration.ofHours(8)));
        caches.add(buildCache("LogVehiculo", Duration.ofHours(8)));
        
        caches.add(buildCache("Publicidad", Duration.ofMinutes(6)));
        caches.add(buildCache("PublicidadArchivo", Duration.ofMinutes(6)));
        
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);

        return cacheManager;
    }
    
}
