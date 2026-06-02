package co.edu.usco.peerlink.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de documentacion OpenAPI/Swagger para la API REST.
 *
 * <p>Describe los modulos funcionales, esquemas de seguridad JWT/cookie,
 * roles, claves tecnicas y flujos principales implementados en PeerLink.</p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PeerLink API",
                version = "1.3",
                description = """
                        API REST de PeerLink para la gestion de tutorias academicas entre estudiantes, tutores y administradores.

                        La autenticacion se realiza con JWT generado en /api/auth/login. En el flujo web real, el backend entrega el JWT en la cookie segura AUTH_TOKEN con HttpOnly, Secure y SameSite=Lax. El filtro de seguridad tambien acepta Authorization: Bearer <token> para pruebas tecnicas desde clientes HTTP o Swagger cuando se use un token valido.

                        La seguridad y los roles se consultan desde las tablas satelite usuarios_correo, usuarios_password y usuarios_rol. El catalogo y las reservas respetan el modelo 6NF, incluyendo idioma, facultad, fecha/hora, estados, asignaciones tutor-materia y reportes PDF.

                        Flujos principales implementados:
                        - Autenticacion JWT con cookie segura HttpOnly AUTH_TOKEN.
                        - Registro publico de estudiantes/tutores y gestion administrativa de usuarios.
                        - Politica de contrasenas: 10 a 72 caracteres, minuscula, mayuscula, numero, bloqueo de contrasenas comunes y bloqueo de contrasenas que contienen el correo.
                        - Recuperacion asistida de acceso: el usuario solicita ayuda, soporte recibe correo por SMTP/Mailtrap y el ADMIN puede restablecer una contrasena temporal tras verificar identidad.
                        - Gestion 6NF de materias con idioma y facultad como claves tecnicas estables.
                        - Publicacion de tutorias por tutores, reservas por estudiantes, filtros por idioma/facultad/fecha-hora y horarios semanales.
                        - Notificaciones por correo para creacion de reservas y cambios de estado.
                        - Reportes PDF con JasperReports para estudiante, tutor y administrador.
                        - Auditoria persistente en PostgreSQL para entidades principales mediante tablas y triggers.

                        Roles principales:
                        - ADMIN: gestiona usuarios, materias, asignaciones, reportes administrativos y Swagger.
                        - TUTOR: crea tutorias, consulta solicitudes, responde reservas y descarga su reporte.
                        - ESTUDIANTE: solicita o acepta tutorias, consulta su horario/reservas y descarga su reporte.

                        Claves tecnicas admitidas:
                        - Idiomas: es, en, pt.
                        - Facultades: CIENCIAS_EXACTAS_Y_NATURALES, CIENCIAS_JURIDICAS_Y_POLITICAS, CIENCIAS_SOCIALES_Y_HUMANAS, ECONOMIA_Y_ADMINISTRACION, EDUCACION, INGENIERIA, SALUD.
                        """,
                contact = @Contact(name = "PeerLink", email = "admin@peerlink.edu.co"),
                license = @License(name = "Uso academico")
        ),
        tags = {
                @Tag(name = "Autenticacion", description = "Login, logout, cookie JWT y recuperacion asistida de acceso."),
                @Tag(name = "Usuarios", description = "Registro, perfil, gestion administrativa y restablecimiento de contrasenas por ADMIN."),
                @Tag(name = "Materias", description = "Catalogo 6NF de materias, idioma, facultad, asignaciones y tutorias publicadas."),
                @Tag(name = "Reservas", description = "Solicitudes, reservas, estados, filtros y soporte para horario semanal."),
                @Tag(name = "Reportes", description = "Descarga de reportes PDF generados con JasperReports Library.")
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER,
                description = "Autenticacion tecnica opcional para pruebas: enviar Authorization: Bearer <token>. El flujo web principal usa cookie HttpOnly."
        ),
        @SecurityScheme(
                name = "cookieAuth",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.COOKIE,
                paramName = "AUTH_TOKEN",
                description = "Cookie HttpOnly creada por /api/auth/login. En navegador se envia automaticamente; no se edita desde JavaScript."
        )
}
)
public class OpenApiConfig {
}
