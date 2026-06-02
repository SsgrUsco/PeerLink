package co.edu.usco.peerlink.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Configura la internacionalizacion manual de PeerLink para espanol, ingles y portugues.
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /**
     * Define el resolvedor de idioma basado en el parametro {@code lang}.
     *
     * @return resolvedor con idioma por defecto espanol
     */
    @Bean
    public LocaleResolver localeResolver() {
        QueryParamLocaleResolver localeResolver = new QueryParamLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("es"));
        localeResolver.setSupportedLocales(List.of(
                Locale.forLanguageTag("es"),
                Locale.forLanguageTag("en"),
                Locale.forLanguageTag("pt")
        ));
        return localeResolver;
    }

    @Bean
    /**
     * Interceptor que permite cambiar el idioma con el parametro {@code lang}.
     *
     * @return interceptor de cambio de idioma
     */
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    /**
     * Fuente de mensajes para validaciones y errores traducibles.
     *
     * @return message source basado en archivos {@code messages_*.properties}
     */
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        source.setFallbackToSystemLocale(false);
        return source;
    }

    @Bean
    /**
     * Integra Bean Validation con los mensajes i18n del proyecto.
     *
     * @param messageSource fuente de mensajes configurada
     * @return validador localizado
     */
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

    @Override
    /**
     * Registra el interceptor de idioma en Spring MVC.
     *
     * @param registry registro de interceptores MVC
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
