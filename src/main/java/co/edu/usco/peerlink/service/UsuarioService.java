package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.dto.UsuarioGestionDTO;
import co.edu.usco.peerlink.dto.UsuarioPasswordUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioPerfilUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO crearUsuario(UsuarioRegistroDTO dto);
    UsuarioDTO crearUsuario(UsuarioGestionDTO dto);
    List<UsuarioDTO> obtenerTodos();
    void eliminarUsuario(Integer id);
    UsuarioDTO obtenerPerfilActual();
    UsuarioDTO actualizarPerfilActual(UsuarioPerfilUpdateDTO dto);
    void actualizarPasswordActual(UsuarioPasswordUpdateDTO dto);
}
