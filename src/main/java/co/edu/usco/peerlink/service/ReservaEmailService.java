package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.model.Reserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Envia notificaciones por correo asociadas a creacion y cambios de reservas.
 */
@Service
public class ReservaEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservaEmailService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "EEEE d 'de' MMMM 'de' yyyy, h:mm a",
            Locale.of("es", "CO")
    );

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final boolean enabled;
    private final String from;

    /**
     * @param mailSenderProvider proveedor SMTP configurado por Spring
     * @param enabled bandera que activa o desactiva el envio real
     * @param from remitente usado en los correos
     */
    public ReservaEmailService(ObjectProvider<JavaMailSender> mailSenderProvider,
                               @Value("${peerlink.mail.enabled:false}") boolean enabled,
                               @Value("${peerlink.mail.from:no-reply@peerlink.edu.co}") String from) {
        this.mailSenderProvider = mailSenderProvider;
        this.enabled = enabled;
        this.from = from;
    }

    /**
     * Notifica a estudiante y tutor cuando se crea una reserva.
     *
     * @param reserva reserva creada
     */
    public void notifyReservationCreated(Reserva reserva) {
        if (!enabled) {
            return;
        }

        ReservationMailData data = ReservationMailData.from(reserva);
        afterCommit(() -> {
            send(
                    data.tutorEmail(),
                    "Nueva solicitud de tutoria en PeerLink",
                    """
                            Hola %s,

                            Recibiste una nueva solicitud de tutoria en PeerLink.

                            Estudiante: %s
                            Materia: %s
                            Fecha y hora: %s
                            Idioma: %s
                            Facultad: %s

                            Ingresa a tu panel para confirmarla o cancelarla.
                            """.formatted(
                            data.tutorName(),
                            data.studentName(),
                            data.subjectName(),
                            formatDate(data.dateTime()),
                            data.language(),
                            data.faculty()
                    )
            );

            send(
                    data.studentEmail(),
                    "Tu solicitud de tutoria fue registrada",
                    """
                            Hola %s,

                            Tu solicitud de tutoria fue registrada correctamente y quedo pendiente de respuesta del tutor.

                            Tutor: %s
                            Materia: %s
                            Fecha y hora: %s
                            Idioma: %s
                            Facultad: %s

                            Te notificaremos cuando el tutor actualice el estado.
                            """.formatted(
                            data.studentName(),
                            data.tutorName(),
                            data.subjectName(),
                            formatDate(data.dateTime()),
                            data.language(),
                            data.faculty()
                    )
            );
        });
    }

    /**
     * Notifica a estudiante y tutor cuando cambia el estado de una reserva.
     *
     * @param reserva reserva actualizada
     * @param previousStatus estado anterior
     * @param newStatus nuevo estado
     */
    public void notifyReservationStatusChanged(Reserva reserva, String previousStatus, String newStatus) {
        if (!enabled || previousStatus.equals(newStatus)) {
            return;
        }

        ReservationMailData data = ReservationMailData.from(reserva);
        afterCommit(() -> {
            send(
                    data.studentEmail(),
                    "Tu tutoria fue %s".formatted(statusLabel(newStatus)),
                    """
                            Hola %s,

                            El estado de tu tutoria cambio de %s a %s.

                            Tutor: %s
                            Materia: %s
                            Fecha y hora: %s
                            Idioma: %s
                            Facultad: %s

                            Puedes revisar el detalle actualizado desde tu panel de estudiante.
                            """.formatted(
                            data.studentName(),
                            statusLabel(previousStatus),
                            statusLabel(newStatus),
                            data.tutorName(),
                            data.subjectName(),
                            formatDate(data.dateTime()),
                            data.language(),
                            data.faculty()
                    )
            );

            send(
                    data.tutorEmail(),
                    "Actualizaste una tutoria en PeerLink",
                    """
                            Hola %s,

                            Se registro el cambio de estado de una tutoria de %s a %s.

                            Estudiante: %s
                            Materia: %s
                            Fecha y hora: %s
                            Idioma: %s
                            Facultad: %s

                            Puedes revisar la solicitud desde tu panel de tutor.
                            """.formatted(
                            data.tutorName(),
                            statusLabel(previousStatus),
                            statusLabel(newStatus),
                            data.studentName(),
                            data.subjectName(),
                            formatDate(data.dateTime()),
                            data.language(),
                            data.faculty()
                    )
            );
        });
    }

    private void send(String to, String subject, String body) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            LOGGER.warn("No hay JavaMailSender configurado. No se envio correo a {}.", to);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException exception) {
            LOGGER.warn("No fue posible enviar correo a {}: {}", to, exception.getMessage());
        }
    }

    private void afterCommit(Runnable action) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            action.run();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? "Sin fecha definida" : DATE_FORMATTER.format(dateTime);
    }

    private String statusLabel(String status) {
        return switch (status == null ? "" : status) {
            case "CONFIRMADA" -> "confirmada";
            case "CANCELADA" -> "cancelada";
            case "PENDIENTE" -> "pendiente";
            default -> status.toLowerCase(Locale.ROOT);
        };
    }

    private record ReservationMailData(
            String studentName,
            String studentEmail,
            String tutorName,
            String tutorEmail,
            String subjectName,
            LocalDateTime dateTime,
            String language,
            String faculty
    ) {
        private static ReservationMailData from(Reserva reserva) {
            var estudiante = reserva.getReservaEstudiante().getEstudiante();
            var tutorMateria = reserva.getReservaTutorMateria().getTutorMateria();
            var tutor = tutorMateria.getTutor();
            var materia = tutorMateria.getMateria();

            return new ReservationMailData(
                    estudiante.getUsuarioNombre().getNombreCompleto(),
                    estudiante.getUsuarioCorreo().getCorreo(),
                    tutor.getUsuarioNombre().getNombreCompleto(),
                    tutor.getUsuarioCorreo().getCorreo(),
                    materia.getNombre(),
                    reserva.getReservaFecha().getFechaHora(),
                    reserva.getReservaIdioma().getIdioma(),
                    reserva.getReservaFacultad().getFacultad()
            );
        }
    }
}
