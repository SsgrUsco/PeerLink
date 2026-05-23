package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.model.*;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        UsuarioNombre nombre = new UsuarioNombre();
        nombre.setNombreCompleto(dto.getNombreCompleto());
        nombre.setUsuario(usuario);
        usuario.setUsuarioNombre(nombre);

        UsuarioCorreo correo = new UsuarioCorreo();
        correo.setCorreo(dto.getCorreo());
        correo.setUsuario(usuario);
        usuario.setUsuarioCorreo(correo);

        UsuarioPassword password = new UsuarioPassword();
        password.setPassword(dto.getPassword());
        password.setUsuario(usuario);
        usuario.setUsuarioPassword(password);

        UsuarioRol rol = new UsuarioRol();
        rol.setRol(dto.getRol());
        rol.setUsuario(usuario);
        usuario.setUsuarioRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);
        dto.setId(guardado.getId());
        return dto;
    }

    @Override
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(u -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(u.getId());
            if (u.getUsuarioNombre() != null) dto.setNombreCompleto(u.getUsuarioNombre().getNombreCompleto());
            if (u.getUsuarioCorreo() != null) dto.setCorreo(u.getUsuarioCorreo().getCorreo());
            if (u.getUsuarioRol() != null) dto.setRol(u.getUsuarioRol().getRol());
            return dto;
        }).collect(Collectors.toList());
    }
}