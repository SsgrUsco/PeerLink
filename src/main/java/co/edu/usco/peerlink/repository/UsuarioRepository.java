package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @EntityGraph(attributePaths = {
            "usuarioNombre",
            "usuarioCorreo",
            "usuarioPassword",
            "usuarioRol"
    })
    Optional<Usuario> findByUsuarioCorreoCorreoIgnoreCase(String correo);

    boolean existsByUsuarioCorreoCorreoIgnoreCase(String correo);
}
