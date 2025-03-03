package abreuapps.core.conf;

import abreuapps.core.control.usuario.UsuarioServ;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author cabreu
 */

@RequiredArgsConstructor
public class AuthProv implements AuthenticationProvider {
    
    private final PasswordEncoder passwordEncoder;
    
    private final LoginAttemptHandler loginAttemptService;

    private final MessageSource messageSrc;

    private final UsuarioServ userServ;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (loginAttemptService.isBlocked())
            throw new UsernameNotFoundException(messageSrc.getMessage("auth.tooManyLoginAttempts",null, LocaleContextHolder.getLocale()));

        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userServ.obtener(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                messageSrc.getMessage("auth.userNotFound",null, LocaleContextHolder.getLocale())
                        )
                );

        if (user == null || !passwordEncoder.matches(password, user.getPassword()))
            throw new UsernameNotFoundException(messageSrc.getMessage("auth.invalidCredentials",null, LocaleContextHolder.getLocale()));


        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
