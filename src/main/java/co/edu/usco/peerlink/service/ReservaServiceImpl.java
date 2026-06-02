package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.ReservaDetalleDTO;
import co.edu.usco.peerlink.dto.ReservaDTO;
import co.edu.usco.peerlink.exception.BusinessException;
import co.edu.usco.peerlink.model.Reserva;
import co.edu.usco.peerlink.model.ReservaEstado;
import co.edu.usco.peerlink.model.ReservaEstudiante;
import co.edu.usco.peerlink.model.ReservaFacultad;
import co.edu.usco.peerlink.model.ReservaFecha;
import co.edu.usco.peerlink.model.ReservaIdioma;
import co.edu.usco.peerlink.model.ReservaTutorMateria;
import co.edu.usco.peerlink.model.TutorMateria;
import co.edu.usco.peerlink.model.TutorMateriaId;
import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.ReservaRepository;
import co.edu.usco.peerlink.repository.TutorMateriaRepository;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import co.edu.usco.peerlink.security.AuthenticatedUser;
import co.edu.usco.peerlink.security.SecurityAuditLogger;
import co.edu.usco.peerlink.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementa la logica de creacion, consulta y cambio de estado de reservas.
 */
@Service
public class ReservaServiceImpl implements ReservaService {
    private static final LocalDateTime FILTER_MIN_DATE = LocalDateTime.of(1900, 1, 1, 0, 0);
    private static final LocalDateTime FILTER_MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorMateriaRepository tutorMateriaRepository;
    private final SecurityAuditLogger auditLogger;
    private final ReservaEmailService reservaEmailService;

    /**
     * Inyecta repositorios, auditoria de seguridad y servicio de correo.
     */
    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              UsuarioRepository usuarioRepository,
                              TutorMateriaRepository tutorMateriaRepository,
                              SecurityAuditLogger auditLogger,
                              ReservaEmailService reservaEmailService) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorMateriaRepository = tutorMateriaRepository;
        this.auditLogger = auditLogger;
        this.reservaEmailService = reservaEmailService;
    }

    @Override
    @Transactional
    /**
     * Crea una reserva en estado pendiente para el estudiante autenticado.
     *
     * @param dto datos de tutor, materia y fecha/hora cuando aplica
     * @return reserva creada
     */
    public ReservaDTO crearReserva(ReservaDTO dto) {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"ESTUDIANTE".equals(currentUser.getRol())) {
            throw new BusinessException("error.reserva.soloEstudiante", HttpStatus.FORBIDDEN);
        }

        Usuario estudiante = usuarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new BusinessException("error.usuario.noEncontrado", HttpStatus.NOT_FOUND));

        TutorMateriaId tmId = new TutorMateriaId(dto.getTutorId(), dto.getMateriaId());
        TutorMateria tutorMateria = tutorMateriaRepository.findById(tmId)
                .orElseThrow(() -> new BusinessException("error.reserva.tutorMateriaInvalida", HttpStatus.BAD_REQUEST));

        Reserva reserva = new Reserva();

        ReservaEstudiante reservaEstudiante = new ReservaEstudiante();
        reservaEstudiante.setEstudiante(estudiante);
        reservaEstudiante.setReserva(reserva);
        reserva.setReservaEstudiante(reservaEstudiante);

        ReservaTutorMateria reservaTutorMateria = new ReservaTutorMateria();
        reservaTutorMateria.setTutorMateria(tutorMateria);
        reservaTutorMateria.setReserva(reserva);
        reserva.setReservaTutorMateria(reservaTutorMateria);

        LocalDateTime fechaHoraPublicada = tutorMateria.getTutorMateriaFecha() == null
                ? null
                : tutorMateria.getTutorMateriaFecha().getFechaHora();
        LocalDateTime fechaHoraReserva = fechaHoraPublicada != null ? fechaHoraPublicada : dto.getFechaHora();
        if (fechaHoraReserva == null) {
            throw new BusinessException("error.reserva.tutorMateriaInvalida", HttpStatus.BAD_REQUEST);
        }
        if (!fechaHoraReserva.isAfter(LocalDateTime.now())) {
            throw new BusinessException("error.reserva.fechaHoraPasada", HttpStatus.BAD_REQUEST);
        }

        ReservaFecha reservaFecha = new ReservaFecha();
        reservaFecha.setFechaHora(fechaHoraReserva);
        reservaFecha.setReserva(reserva);
        reserva.setReservaFecha(reservaFecha);

        ReservaEstado reservaEstado = new ReservaEstado();
        reservaEstado.setEstado("PENDIENTE");
        reservaEstado.setReserva(reserva);
        reserva.setReservaEstado(reservaEstado);

        ReservaIdioma reservaIdioma = new ReservaIdioma();
        reservaIdioma.setIdioma(tutorMateria.getMateria().getMateriaIdioma().getIdioma());
        reservaIdioma.setReserva(reserva);
        reserva.setReservaIdioma(reservaIdioma);

        ReservaFacultad reservaFacultad = new ReservaFacultad();
        reservaFacultad.setFacultad(tutorMateria.getMateria().getMateriaFacultad().getFacultad());
        reservaFacultad.setReserva(reserva);
        reserva.setReservaFacultad(reservaFacultad);

        Reserva reservaGuardada = reservaRepository.save(reserva);
        reservaEmailService.notifyReservationCreated(reservaGuardada);
        return toDto(reservaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Obtiene reservas resumidas segun el rol del usuario autenticado.
     *
     * @return reservas del estudiante o tutor actual
     */
    public List<ReservaDTO> obtenerReservasDelUsuario() {
        AuthenticatedUser currentUser = getCurrentUser();
        List<Reserva> reservas = switch (currentUser.getRol()) {
            case "ESTUDIANTE" -> reservaRepository.findAllByEstudianteId(currentUser.getId(), "", "", FILTER_MIN_DATE, FILTER_MAX_DATE);
            case "TUTOR" -> reservaRepository.findAllByTutorId(currentUser.getId(), "", "", FILTER_MIN_DATE, FILTER_MAX_DATE);
            default -> throw new BusinessException("error.reserva.rolSinAcceso", HttpStatus.FORBIDDEN);
        };

        return reservas.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Consulta reservas detalladas del estudiante con filtros opcionales.
     *
     * @param idioma filtro por idioma
     * @param facultad filtro por facultad
     * @param desde fecha inicial
     * @param hasta fecha final
     * @return reservas detalladas del estudiante
     */
    public List<ReservaDetalleDTO> obtenerMisReservas(String idioma, String facultad,
                                                      LocalDateTime desde, LocalDateTime hasta) {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"ESTUDIANTE".equals(currentUser.getRol())) {
            throw new BusinessException("error.reserva.soloEstudianteConsulta", HttpStatus.FORBIDDEN);
        }

        return reservaRepository.findAllByEstudianteId(
                        currentUser.getId(),
                        normalizeFilter(idioma),
                        normalizeFilter(facultad),
                        normalizeFrom(desde),
                        normalizeUntil(hasta)
                ).stream()
                .map(this::toDetalleDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Consulta solicitudes y tutorias del tutor autenticado con filtros opcionales.
     *
     * @param idioma filtro por idioma
     * @param facultad filtro por facultad
     * @param desde fecha inicial
     * @param hasta fecha final
     * @return reservas detalladas asociadas al tutor
     */
    public List<ReservaDetalleDTO> obtenerMisTutorias(String idioma, String facultad,
                                                      LocalDateTime desde, LocalDateTime hasta) {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"TUTOR".equals(currentUser.getRol())) {
            throw new BusinessException("error.reserva.soloTutorConsulta", HttpStatus.FORBIDDEN);
        }

        return reservaRepository.findAllByTutorId(
                        currentUser.getId(),
                        normalizeFilter(idioma),
                        normalizeFilter(facultad),
                        normalizeFrom(desde),
                        normalizeUntil(hasta)
                ).stream()
                .map(this::toDetalleDto)
                .toList();
    }

    @Override
    @Transactional
    /**
     * Actualiza el estado de una reserva perteneciente al tutor autenticado.
     *
     * @param reservaId identificador de la reserva
     * @param nuevoEstado estado solicitado
     * @return reserva actualizada
     */
    public ReservaDTO actualizarEstado(Integer reservaId, String nuevoEstado) {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"TUTOR".equals(currentUser.getRol())) {
            throw new BusinessException("error.reserva.soloTutor", HttpStatus.FORBIDDEN);
        }

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new BusinessException("error.reserva.noEncontrada", HttpStatus.NOT_FOUND));

        Integer tutorId = reserva.getReservaTutorMateria().getTutorMateria().getTutor().getId();
        if (!currentUser.getId().equals(tutorId)) {
            throw new BusinessException("error.reserva.tutorNoAutorizado", HttpStatus.FORBIDDEN);
        }

        String estadoAnterior = reserva.getReservaEstado().getEstado();
        reserva.getReservaEstado().setEstado(nuevoEstado);
        auditLogger.log(
                "RESERVATION_STATUS_CHANGED",
                currentUser.getUsername(),
                "reservaId=%s from=%s to=%s".formatted(reservaId, estadoAnterior, nuevoEstado)
        );
        Reserva reservaGuardada = reservaRepository.save(reserva);
        reservaEmailService.notifyReservationStatusChanged(reservaGuardada, estadoAnterior, nuevoEstado);
        return toDto(reservaGuardada);
    }

    private ReservaDTO toDto(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setEstudianteId(reserva.getReservaEstudiante().getEstudiante().getId());
        dto.setTutorId(reserva.getReservaTutorMateria().getTutorMateria().getTutor().getId());
        dto.setMateriaId(reserva.getReservaTutorMateria().getTutorMateria().getMateria().getId());
        dto.setFechaHora(reserva.getReservaFecha().getFechaHora());
        dto.setIdioma(reserva.getReservaIdioma().getIdioma());
        dto.setFacultad(reserva.getReservaFacultad().getFacultad());
        dto.setEstado(reserva.getReservaEstado().getEstado());
        return dto;
    }

    private ReservaDetalleDTO toDetalleDto(Reserva reserva) {
        ReservaDetalleDTO dto = new ReservaDetalleDTO();
        dto.setId(reserva.getId());
        dto.setEstudianteId(reserva.getReservaEstudiante().getEstudiante().getId());
        dto.setEstudianteNombre(reserva.getReservaEstudiante().getEstudiante().getUsuarioNombre().getNombreCompleto());
        dto.setTutorId(reserva.getReservaTutorMateria().getTutorMateria().getTutor().getId());
        dto.setTutorNombre(reserva.getReservaTutorMateria().getTutorMateria().getTutor().getUsuarioNombre().getNombreCompleto());
        dto.setMateriaId(reserva.getReservaTutorMateria().getTutorMateria().getMateria().getId());
        dto.setMateriaNombre(reserva.getReservaTutorMateria().getTutorMateria().getMateria().getNombre());
        dto.setFechaHora(reserva.getReservaFecha().getFechaHora());
        dto.setIdioma(reserva.getReservaIdioma().getIdioma());
        dto.setFacultad(reserva.getReservaFacultad().getFacultad());
        dto.setEstado(reserva.getReservaEstado().getEstado());
        return dto;
    }

    private AuthenticatedUser getCurrentUser() {
        return SecurityUtils.currentUser();
    }

    private String normalizeFilter(String value) {
        return value == null ? "" : value.trim();
    }

    private LocalDateTime normalizeFrom(LocalDateTime value) {
        return value == null ? FILTER_MIN_DATE : value;
    }

    private LocalDateTime normalizeUntil(LocalDateTime value) {
        return value == null ? FILTER_MAX_DATE : value;
    }
}
