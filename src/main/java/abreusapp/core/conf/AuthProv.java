package abreusapp.core.conf;

import abreusapp.core.control.utils.LoginAttemptServ;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author cabreu
 */

@RequiredArgsConstructor
public class AuthProv implements AuthenticationProvider {
    
    
    private final UserDetailsService userDetailsService;
    
    private final PasswordEncoder passwordEncoder;
    
    private final LoginAttemptServ loginAttemptService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (loginAttemptService.isBlocked()) {
            throw new UsernameNotFoundException("Has sido bloqueado! Demasiados intentos de sesión.");
        }
        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("Usuario o Contraseña invalidos!");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
