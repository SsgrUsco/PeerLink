package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.dto.UsuarioGestionDTO;
import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO crearUsuario(UsuarioRegistroDTO dto);
    UsuarioDTO crearUsuario(UsuarioGestionDTO dto);
    List<UsuarioDTO> obtenerTodos();
    void eliminarUsuario(Integer id);
}
