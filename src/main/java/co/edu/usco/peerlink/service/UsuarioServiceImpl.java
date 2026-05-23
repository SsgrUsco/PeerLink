package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;
import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioRegistroDTO registroDTO) {

        // 1. Validación: Verificar si el correo ya existe en la base de datos
        if (usuarioRepository.findByCorreo(registroDTO.getCorreo()).isPresent()) {
            throw new RuntimeException("Error: El correo ingresado ya se encuentra registrado.");
        }

        // 2. Transformación: Convertir el DTO a la Entidad y encriptar la contraseña
        Usuario nuevoUsuario = Usuario.builder()
                .nombreCompleto(registroDTO.getNombreCompleto())
                .correo(registroDTO.getCorreo())
                .password(passwordEncoder.encode(registroDTO.getPassword())) // <-- Aplicación de Bcrypt
                .rol("ESTUDIANTE") // Se asigna el rol base por defecto
                .build();

        // 3. Persistencia: Guardar en PostgreSQL
        return usuarioRepository.save(nuevoUsuario);
    }
}