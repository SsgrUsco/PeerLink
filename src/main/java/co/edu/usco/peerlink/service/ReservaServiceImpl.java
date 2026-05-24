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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {
    private static final LocalDateTime FILTER_MIN_DATE = LocalDateTime.of(1900, 1, 1, 0, 0);
    private static final LocalDateTime FILTER_MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorMateriaRepository tutorMateriaRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              UsuarioRepository usuarioRepository,
                              TutorMateriaRepository tutorMateriaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorMateriaRepository = tutorMateriaRepository;
    }

    @Override
    @Transactional
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

        ReservaFecha reservaFecha = new ReservaFecha();
        reservaFecha.setFechaHora(dto.getFechaHora());
        reservaFecha.setReserva(reserva);
        reserva.setReservaFecha(reservaFecha);

        ReservaEstado reservaEstado = new ReservaEstado();
        reservaEstado.setEstado("PENDIENTE");
        reservaEstado.setReserva(reserva);
        reserva.setReservaEstado(reservaEstado);

        ReservaIdioma reservaIdioma = new ReservaIdioma();
        reservaIdioma.setIdioma(tutorMateria.getTutorMateriaIdioma().getIdioma());
        reservaIdioma.setReserva(reserva);
        reserva.setReservaIdioma(reservaIdioma);

        ReservaFacultad reservaFacultad = new ReservaFacultad();
        reservaFacultad.setFacultad(tutorMateria.getTutorMateriaFacultad().getFacultad());
        reservaFacultad.setReserva(reserva);
        reserva.setReservaFacultad(reservaFacultad);

        return toDto(reservaRepository.save(reserva));
    }

    @Override
    @Transactional(readOnly = true)
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

        reserva.getReservaEstado().setEstado(nuevoEstado);
        return toDto(reservaRepository.save(reserva));
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
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
