package co.edu.usco.peerlink.service;
import co.edu.usco.peerlink.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    UsuarioDTO crearUsuario(UsuarioDTO dto);
    List<UsuarioDTO> obtenerTodos();
}