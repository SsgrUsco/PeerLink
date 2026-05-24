package co.edu.usco.peerlink.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PeerLink API",
                version = "1.0",
                description = "API REST para la gestión de tutorías con autenticación JWT, roles ADMIN/TUTOR/ESTUDIANTE, i18n y validaciones.",
                contact = @Contact(name = "PeerLink", email = "admin@peerlink.edu.co"),
                license = @License(name = "Uso académico")
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Token JWT obtenido desde /api/auth/login. Pégalo en Swagger UI usando el botón Authorize."
)
public class OpenApiConfig {
}
