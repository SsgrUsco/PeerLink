package co.edu.usco.peerlink.security;

import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
