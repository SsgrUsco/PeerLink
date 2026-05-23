package co.edu.usco.peerlink.service;
import co.edu.usco.peerlink.dto.ReservaDTO;
import java.util.List;

public interface ReservaService {
    ReservaDTO crearReserva(ReservaDTO dto);
    List<ReservaDTO> obtenerTodas();
}