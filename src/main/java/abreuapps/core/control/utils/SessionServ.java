package abreuapps.core.control.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServ {

    private final SessionRegistry sessionRegistry;


    public void cerrarSesion(String usuario){
        sessionRegistry
                .getAllPrincipals()
                .stream()
                .filter(principal -> principal instanceof UserDetails)
                .map(principal -> (UserDetails) principal)
                .filter(userDetails -> userDetails.getUsername().equals(usuario))
                .forEach(userDetails ->
                        sessionRegistry
                                .getAllSessions(userDetails, false)
                                .forEach(SessionInformation::expireNow)
                );
    }

}
