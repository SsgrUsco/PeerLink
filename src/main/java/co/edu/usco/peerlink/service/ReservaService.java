package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.ReservaDetalleDTO;
import co.edu.usco.peerlink.dto.ReservaDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaService {
    ReservaDTO crearReserva(ReservaDTO dto);
    List<ReservaDTO> obtenerReservasDelUsuario();
    List<ReservaDetalleDTO> obtenerMisReservas(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);
    List<ReservaDetalleDTO> obtenerMisTutorias(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);
    ReservaDTO actualizarEstado(Integer reservaId, String nuevoEstado);
}
