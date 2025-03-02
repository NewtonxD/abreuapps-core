package abreuapps.core.conf;

import abreuapps.core.control.utils.Localizer;
import gg.jte.support.LocalizationSupport;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Configuration
public class LocalizationConf {

    public static class JteMessageLocale implements LocalizationSupport {
        private final MessageSource messageSource;
        private final Locale locale;

        public JteMessageLocale(MessageSource messageSource, Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        @Override
        public String lookup(String key) {
            return messageSource.getMessage(key, null, locale);
        }
    }

    @Component
    public static class ModelLocalizationInterceptor implements HandlerInterceptor {
        private final Localizer localizationSupport;

        public ModelLocalizationInterceptor(Localizer localizationSupport) {
            this.localizationSupport = localizationSupport;
        }

        @Override
        public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
        ) {
            if (modelAndView != null) {
                modelAndView.getModel().put("localize", localizationSupport);
            }
        }


    }

}
