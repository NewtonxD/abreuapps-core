package abreuapps.core.conf;

/**
 *
 * @author cabreu
 */
import abreuapps.core.control.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        
        var user = (Usuario) authentication.getPrincipal();

        if (user.isCredentialsNonExpired())
            response.sendRedirect("/main/index");
        else
            response.sendRedirect("/main/changePwd");
    }
}

