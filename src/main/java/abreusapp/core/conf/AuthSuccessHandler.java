package abreusapp.core.conf;

/**
 *
 * @author cabreu
 */
import abreusapp.core.control.usuario.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        Usuario user = (Usuario) authentication.getPrincipal();
        
        if (user != null) {
            if (user.isCredentialsNonExpired()) {
                response.sendRedirect("/main/index");
            } else {
                response.sendRedirect("/main/changePwd");
            }
        } else {
            // Handle case when user is not found
            response.sendRedirect("/auth/login?error=true");
        }
    }
}

