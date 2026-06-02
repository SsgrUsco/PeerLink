package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.dto.AdminPasswordResetDTO;
import co.edu.usco.peerlink.dto.UsuarioGestionDTO;
import co.edu.usco.peerlink.dto.UsuarioPasswordUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioPerfilUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;

import java.util.List;

/**
 * Contrato de negocio para la gestion de usuarios de PeerLink.
 *
 * <p>Centraliza operaciones de registro publico, administracion de usuarios,
 * perfil autenticado y cambios de contrasena. La implementacion respeta el
 * modelo 6NF del proyecto al persistir nombre, correo, password y rol en
 * tablas satelite separadas.</p>
 */
public interface UsuarioService {
    /**
     * Crea una cuenta publica para estudiante o tutor.
     *
     * @param dto datos enviados desde el formulario publico de registro
     * @return usuario creado sin exponer la contrasena
     */
    UsuarioDTO crearUsuario(UsuarioRegistroDTO dto);

    /**
     * Crea un usuario desde el panel administrativo.
     *
     * @param dto datos administrativos, incluyendo rol
     * @return usuario creado sin exponer la contrasena
     */
    UsuarioDTO crearUsuario(UsuarioGestionDTO dto);

    /**
     * Obtiene todos los usuarios registrados para administracion.
     *
     * @return lista de usuarios del sistema
     */
    List<UsuarioDTO> obtenerTodos();

    /**
     * Elimina un usuario cuando no existen restricciones de negocio que lo impidan.
     *
     * @param id identificador del usuario a eliminar
     */
    void eliminarUsuario(Integer id);

    /**
     * Devuelve el perfil del usuario autenticado.
     *
     * @return datos del usuario actual
     */
    UsuarioDTO obtenerPerfilActual();

    /**
     * Actualiza nombre y correo del usuario autenticado.
     *
     * @param dto datos editables del perfil
     * @return perfil actualizado
     */
    UsuarioDTO actualizarPerfilActual(UsuarioPerfilUpdateDTO dto);

    /**
     * Cambia la contrasena del usuario autenticado validando la contrasena actual.
     *
     * @param dto contrasena actual y contrasena nueva
     */
    void actualizarPasswordActual(UsuarioPasswordUpdateDTO dto);

    /**
     * Permite a un administrador definir una contrasena temporal para un usuario.
     *
     * @param id identificador del usuario objetivo
     * @param dto nueva contrasena temporal
     */
    void restablecerPasswordPorAdmin(Integer id, AdminPasswordResetDTO dto);
}
