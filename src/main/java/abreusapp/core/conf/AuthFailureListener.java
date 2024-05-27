package abreusapp.core.conf;

import abreusapp.core.control.utils.LoginAttemptServ;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */

@Component
public class AuthFailureListener implements 
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private LoginAttemptServ loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            loginAttemptService.loginFailed(request.getRemoteAddr());
        } else {
            loginAttemptService.loginFailed(xfHeader.split(",")[0]);
        }
    }
}
