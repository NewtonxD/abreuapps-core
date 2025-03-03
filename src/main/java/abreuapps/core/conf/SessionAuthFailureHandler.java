package abreuapps.core.conf;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionAuthFailureHandler implements AuthenticationFailureHandler {

    private final MessageSource messageSrc;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception
    ) throws IOException {
        String errorMessage = switch (exception.getClass().getSimpleName()){
            case "BadCredentialsException" -> messageSrc.getMessage("auth.invalidCredentials",null, LocaleContextHolder.getLocale());
            case "LockedException" -> messageSrc.getMessage("auth.lockedAccount",null, LocaleContextHolder.getLocale());
            case "DisabledException" -> messageSrc.getMessage("auth.disabledAccount",null, LocaleContextHolder.getLocale());
            case "AccountExpiredException" -> messageSrc.getMessage("auth.expiredAccount",null, LocaleContextHolder.getLocale());
            case "CredentialsExpiredException" -> messageSrc.getMessage("auth.expiredCredentials",null, LocaleContextHolder.getLocale());
            default -> messageSrc.getMessage("auth.failedAuthentication",null, LocaleContextHolder.getLocale());
        };
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", errorMessage);
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
}
