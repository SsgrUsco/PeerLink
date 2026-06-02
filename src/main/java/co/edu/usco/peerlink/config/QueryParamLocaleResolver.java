package co.edu.usco.peerlink.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Resuelve el idioma de la solicitud desde el parametro {@code lang}.
 */
public class QueryParamLocaleResolver extends AcceptHeaderLocaleResolver {

    /**
     * Prioriza el parametro {@code lang} sobre el encabezado {@code Accept-Language}.
     *
     * @param request solicitud HTTP
     * @return locale soportado o el locale por defecto
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getParameter("lang");
        if (StringUtils.hasText(language)) {
            Locale requestedLocale = Locale.forLanguageTag(language);
            if (getSupportedLocales().contains(requestedLocale)) {
                return requestedLocale;
            }
        }
        return super.resolveLocale(request);
    }

    @Override
    /**
     * No persiste el idioma porque se resuelve por solicitud.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @param locale locale solicitado
     */
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // The locale is resolved per request from the "lang" parameter, so no session or cookie is needed.
    }
}
