package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.ReservaDTO;
import co.edu.usco.peerlink.model.*;
import co.edu.usco.peerlink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TutorMateriaRepository tutorMateriaRepository;

    @Override
    public ReservaDTO crearReserva(ReservaDTO dto) {
        Reserva reserva = new Reserva();

        Usuario estudiante = usuarioRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        ReservaEstudiante re = new ReservaEstudiante();
        re.setEstudiante(estudiante);
        re.setReserva(reserva);
        reserva.setReservaEstudiante(re);

        TutorMateriaId tmId = new TutorMateriaId(dto.getTutorId(), dto.getMateriaId());
        TutorMateria tm = tutorMateriaRepository.findById(tmId)
                .orElseThrow(() -> new RuntimeException("El tutor no dicta esa materia"));
        ReservaTutorMateria rtm = new ReservaTutorMateria();
        rtm.setTutorMateria(tm);
        rtm.setReserva(reserva);
        reserva.setReservaTutorMateria(rtm);

        ReservaFecha rf = new ReservaFecha();
        rf.setFechaHora(dto.getFechaHora());
        rf.setReserva(reserva);
        reserva.setReservaFecha(rf);

        ReservaEstado reEst = new ReservaEstado();
        reEst.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        reEst.setReserva(reserva);
        reserva.setReservaEstado(reEst);

        Reserva guardada = reservaRepository.save(reserva);
        dto.setId(guardada.getId());
        dto.setEstado(guardada.getReservaEstado().getEstado());
        return dto;
    }

    @Override
    public List<ReservaDTO> obtenerTodas() {
        return reservaRepository.findAll().stream().map(r -> {
            ReservaDTO dto = new ReservaDTO();
            dto.setId(r.getId());
            if (r.getReservaEstudiante() != null) dto.setEstudianteId(r.getReservaEstudiante().getEstudiante().getId());
            if (r.getReservaTutorMateria() != null && r.getReservaTutorMateria().getTutorMateria() != null) {
                dto.setTutorId(r.getReservaTutorMateria().getTutorMateria().getTutor().getId());
                dto.setMateriaId(r.getReservaTutorMateria().getTutorMateria().getMateria().getId());
            }
            if (r.getReservaFecha() != null) dto.setFechaHora(r.getReservaFecha().getFechaHora());
            if (r.getReservaEstado() != null) dto.setEstado(r.getReservaEstado().getEstado());
            return dto;
        }).collect(Collectors.toList());
    }
}