package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de usuarios construido sobre la entidad raiz y sus tablas satelite.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por correo ignorando mayusculas y cargando sus satelites principales.
     *
     * @param correo correo ingresado por el usuario
     * @return usuario detallado si existe
     */
    @EntityGraph(attributePaths = {
            "usuarioNombre",
            "usuarioCorreo",
            "usuarioPassword",
            "usuarioRol"
    })
    Optional<Usuario> findByUsuarioCorreoCorreoIgnoreCase(String correo);

    /**
     * Busca un usuario por id cargando nombre, correo, contrasena y rol.
     *
     * @param id identificador del usuario
     * @return usuario detallado si existe
     */
    @EntityGraph(attributePaths = {
            "usuarioNombre",
            "usuarioCorreo",
            "usuarioPassword",
            "usuarioRol"
    })
    Optional<Usuario> findDetailedById(Integer id);

    /**
     * Valida si existe un correo registrado.
     *
     * @param correo correo a verificar
     * @return {@code true} cuando el correo ya existe
     */
    boolean existsByUsuarioCorreoCorreoIgnoreCase(String correo);

    /**
     * Valida duplicidad de correo excluyendo un usuario concreto.
     *
     * @param correo correo a verificar
     * @param id usuario que debe excluirse de la comparacion
     * @return {@code true} si otro usuario ya usa el correo
     */
    boolean existsByUsuarioCorreoCorreoIgnoreCaseAndIdNot(String correo, Integer id);
}
