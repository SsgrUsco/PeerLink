package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;
import co.edu.usco.peerlink.model.Usuario;

public interface UsuarioService {
    Usuario registrarUsuario(UsuarioRegistroDTO registroDTO);
}