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
                version = "1.1",
                description = """
                        API REST de PeerLink para la gestion de tutorias academicas entre estudiantes, tutores y administradores.

                        La autenticacion se realiza mediante JWT obtenido en /api/auth/login. La seguridad y los roles se consultan desde las tablas satelite usuarios_correo, usuarios_password y usuarios_rol. El catalogo y las reservas respetan el modelo 6NF, incluyendo idioma, facultad, fecha/hora, estados, asignaciones tutor-materia y reportes PDF.

                        Roles principales:
                        - ADMIN: gestiona usuarios, materias, asignaciones, reportes administrativos y Swagger.
                        - TUTOR: crea tutorias, consulta solicitudes, responde reservas y descarga su reporte.
                        - ESTUDIANTE: solicita o acepta tutorias, consulta su horario/reservas y descarga su reporte.
                        """,
                contact = @Contact(name = "PeerLink", email = "admin@peerlink.edu.co"),
                license = @License(name = "Uso academico")
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Token JWT obtenido desde /api/auth/login. Pegalo en Swagger UI usando el boton Authorize con el formato: Bearer <token>."
)
public class OpenApiConfig {
}
