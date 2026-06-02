package co.edu.usco.peerlink.security;

import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adaptador entre el modelo 6NF de usuarios y Spring Security.
 *
 * <p>Carga correo, password, rol y nombre desde las tablas satelite
 * {@code usuarios_correo}, {@code usuarios_password}, {@code usuarios_rol}
 * y {@code usuarios_nombre} para construir el principal autenticado.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuarioCorreoCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new AuthenticatedUser(
                usuario.getId(),
                usuario.getUsuarioCorreo().getCorreo(),
                usuario.getUsuarioPassword().getPassword(),
                usuario.getUsuarioRol().getRol(),
                usuario.getUsuarioNombre().getNombreCompleto()
        );
    }
}
