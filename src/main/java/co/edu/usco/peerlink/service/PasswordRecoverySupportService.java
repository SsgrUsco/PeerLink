package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Gestiona solicitudes asistidas de recuperacion de acceso por correo.
 */
@Service
public class PasswordRecoverySupportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordRecoverySupportService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final UsuarioRepository usuarioRepository;
    private final boolean enabled;
    private final String from;
    private final String supportTo;

    /**
     * @param mailSenderProvider proveedor SMTP opcional
     * @param usuarioRepository repositorio para validar existencia sin exponerla al usuario
     * @param enabled indica si el envio SMTP esta activo
     * @param from remitente configurado
     * @param supportTo correo de soporte que recibe las solicitudes
     */
    public PasswordRecoverySupportService(ObjectProvider<JavaMailSender> mailSenderProvider,
                                          UsuarioRepository usuarioRepository,
                                          @Value("${peerlink.mail.enabled:false}") boolean enabled,
                                          @Value("${peerlink.mail.from:no-reply@peerlink.edu.co}") String from,
                                          @Value("${peerlink.mail.support-to:${peerlink.mail.from:no-reply@peerlink.edu.co}}") String supportTo) {
        this.mailSenderProvider = mailSenderProvider;
        this.usuarioRepository = usuarioRepository;
        this.enabled = enabled;
        this.from = from;
        this.supportTo = supportTo;
    }

    /**
     * Registra o envia una solicitud de recuperacion asistida sin revelar si el correo existe.
     *
     * @param correo correo indicado por el usuario
     */
    public void requestAssistedRecovery(String correo) {
        String normalizedEmail = correo.trim().toLowerCase();
        boolean accountExists = usuarioRepository.existsByUsuarioCorreoCorreoIgnoreCase(normalizedEmail);

        if (!enabled) {
            LOGGER.info("Solicitud de recuperación asistida recibida para {}. Mail desactivado.", normalizedEmail);
            return;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            LOGGER.warn("No hay JavaMailSender configurado. No se envió solicitud de recuperación para {}.", normalizedEmail);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(supportTo);
            message.setSubject("PeerLink - solicitud de ayuda para recuperar acceso");
            message.setText("""
                    Hola, equipo de soporte.

                    PeerLink recibió una solicitud de ayuda para recuperar el acceso a una cuenta.

                    Correo ingresado: %s
                    Cuenta registrada en PeerLink: %s

                    Acciones recomendadas:
                    1. Verificar la identidad de la persona solicitante antes de realizar cualquier cambio.
                    2. Si el correo pertenece a una cuenta válida, contactar al usuario por un canal institucional.
                    3. Si corresponde, gestionar el restablecimiento desde administración.

                    Nota de seguridad:
                    Este formulario no cambia contraseñas automáticamente ni confirma al usuario si el correo existe.
                    """.formatted(normalizedEmail, accountExists ? "Sí" : "No"));
            mailSender.send(message);
        } catch (MailException exception) {
            LOGGER.warn("No fue posible enviar la solicitud de recuperación para {}: {}", normalizedEmail, exception.getMessage());
        }
    }
}
