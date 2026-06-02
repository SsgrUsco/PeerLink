package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.AdminPasswordResetDTO;
import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.dto.UsuarioGestionDTO;
import co.edu.usco.peerlink.dto.UsuarioPasswordUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioPerfilUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;
import co.edu.usco.peerlink.exception.BusinessException;
import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.model.UsuarioCorreo;
import co.edu.usco.peerlink.model.UsuarioNombre;
import co.edu.usco.peerlink.model.UsuarioPassword;
import co.edu.usco.peerlink.model.UsuarioRol;
import co.edu.usco.peerlink.repository.ReservaRepository;
import co.edu.usco.peerlink.repository.TutorMateriaRepository;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import co.edu.usco.peerlink.security.AuthenticatedUser;
import co.edu.usco.peerlink.security.SecurityUtils;
import co.edu.usco.peerlink.security.SecurityAuditLogger;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementa la gestion de usuarios respetando las tablas satelite del modelo 6NF.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TutorMateriaRepository tutorMateriaRepository;
    private final ReservaRepository reservaRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityAuditLogger auditLogger;
    private final PasswordPolicyService passwordPolicyService;

    /**
     * Inyecta repositorios, cifrado, auditoria y politica de contrasenas.
     *
     * @param usuarioRepository repositorio de usuarios
     * @param tutorMateriaRepository repositorio de asignaciones tutor-materia
     * @param reservaRepository repositorio de reservas
     * @param passwordEncoder componente de cifrado de contrasenas
     * @param auditLogger logger de eventos de seguridad
     * @param passwordPolicyService validador de politicas de contrasena
     */
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              TutorMateriaRepository tutorMateriaRepository,
                              ReservaRepository reservaRepository,
                              PasswordEncoder passwordEncoder,
                              SecurityAuditLogger auditLogger,
                              PasswordPolicyService passwordPolicyService) {
        this.usuarioRepository = usuarioRepository;
        this.tutorMateriaRepository = tutorMateriaRepository;
        this.reservaRepository = reservaRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogger = auditLogger;
        this.passwordPolicyService = passwordPolicyService;
    }

    @Override
    @Transactional
    /**
     * Registra un usuario desde el flujo publico.
     *
     * @param dto datos de registro
     * @return usuario creado
     */
    public UsuarioDTO crearUsuario(UsuarioRegistroDTO dto) {
        return crearUsuario(dto.getNombreCompleto(), dto.getCorreo(), dto.getPassword(), dto.getRol());
    }

    @Override
    @Transactional
    /**
     * Crea un usuario desde el panel administrador.
     *
     * @param dto datos administrativos
     * @return usuario creado
     */
    public UsuarioDTO crearUsuario(UsuarioGestionDTO dto) {
        return crearUsuario(dto.getNombreCompleto(), dto.getCorreo(), dto.getPassword(), dto.getRol());
    }

    private UsuarioDTO crearUsuario(String nombreCompleto, String correoValue, String passwordValue, String rolValue) {
        if (usuarioRepository.existsByUsuarioCorreoCorreoIgnoreCase(correoValue)) {
            throw new BusinessException("error.usuario.correoDuplicado", HttpStatus.CONFLICT);
        }
        String correoNormalizado = correoValue.trim().toLowerCase();
        passwordPolicyService.validate(passwordValue, correoNormalizado);

        Usuario usuario = new Usuario();

        UsuarioNombre nombre = new UsuarioNombre();
        nombre.setNombreCompleto(nombreCompleto.trim());
        nombre.setUsuario(usuario);
        usuario.setUsuarioNombre(nombre);

        UsuarioCorreo correo = new UsuarioCorreo();
        correo.setCorreo(correoNormalizado);
        correo.setUsuario(usuario);
        usuario.setUsuarioCorreo(correo);

        UsuarioPassword password = new UsuarioPassword();
        password.setPassword(passwordEncoder.encode(passwordValue));
        password.setUsuario(usuario);
        usuario.setUsuarioPassword(password);

        UsuarioRol rol = new UsuarioRol();
        rol.setRol(rolValue.trim().toUpperCase());
        rol.setUsuario(usuario);
        usuario.setUsuarioRol(rol);

        UsuarioDTO creado = toDto(usuarioRepository.save(usuario));
        auditLogger.log("USER_CREATED", correo.getCorreo(), rol.getRol());
        return creado;
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Lista todos los usuarios registrados.
     *
     * @return usuarios ordenados para administracion
     */
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    /**
     * Elimina un usuario si no tiene reservas o asignaciones que lo bloqueen.
     *
     * @param id identificador del usuario
     */
    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("error.usuario.noEncontrado", HttpStatus.NOT_FOUND));

        String rol = usuario.getUsuarioRol() != null ? usuario.getUsuarioRol().getRol() : "";
        if ("ESTUDIANTE".equalsIgnoreCase(rol) && reservaRepository.existsByEstudianteId(id)) {
            throw new BusinessException("error.usuario.conReservas", HttpStatus.CONFLICT);
        }
        if ("TUTOR".equalsIgnoreCase(rol) && reservaRepository.existsByTutorId(id)) {
            throw new BusinessException("error.usuario.conReservas", HttpStatus.CONFLICT);
        }

        if ("TUTOR".equalsIgnoreCase(rol)) {
            tutorMateriaRepository.deleteAllByTutorId(id);
        }
        usuarioRepository.delete(usuario);
        auditLogger.log("USER_DELETED", currentActor(), "targetId=%s role=%s".formatted(id, rol));
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @return datos del perfil actual
     */
    public UsuarioDTO obtenerPerfilActual() {
        return toDto(getCurrentUserEntity());
    }

    @Override
    @Transactional
    /**
     * Actualiza nombre y correo del usuario autenticado.
     *
     * @param dto datos editables del perfil
     * @return perfil actualizado
     */
    public UsuarioDTO actualizarPerfilActual(UsuarioPerfilUpdateDTO dto) {
        Usuario usuario = getCurrentUserEntity();
        String correoNormalizado = dto.getCorreo().trim().toLowerCase();
        if (usuarioRepository.existsByUsuarioCorreoCorreoIgnoreCaseAndIdNot(correoNormalizado, usuario.getId())) {
            throw new BusinessException("error.usuario.correoDuplicado", HttpStatus.CONFLICT);
        }

        usuario.getUsuarioNombre().setNombreCompleto(dto.getNombreCompleto().trim());
        usuario.getUsuarioCorreo().setCorreo(correoNormalizado);
        return toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    /**
     * Cambia la contrasena del usuario autenticado validando la contrasena actual.
     *
     * @param dto contrasena actual y nueva contrasena
     */
    public void actualizarPasswordActual(UsuarioPasswordUpdateDTO dto) {
        Usuario usuario = getCurrentUserEntity();
        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getUsuarioPassword().getPassword())) {
            throw new BusinessException("error.usuario.passwordActualInvalida", HttpStatus.BAD_REQUEST);
        }
        passwordPolicyService.validate(dto.getPasswordNueva(), usuario.getUsuarioCorreo().getCorreo());

        usuario.getUsuarioPassword().setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(usuario);
        auditLogger.log("PASSWORD_CHANGED", usuario.getUsuarioCorreo().getCorreo(), "self-service");
    }

    @Override
    @Transactional
    /**
     * Permite al administrador restablecer la contrasena de un usuario.
     *
     * @param id identificador del usuario
     * @param dto nueva contrasena
     */
    public void restablecerPasswordPorAdmin(Integer id, AdminPasswordResetDTO dto) {
        Usuario usuario = usuarioRepository.findDetailedById(id)
                .orElseThrow(() -> new BusinessException("error.usuario.noEncontrado", HttpStatus.NOT_FOUND));
        passwordPolicyService.validate(dto.getPasswordNueva(), usuario.getUsuarioCorreo().getCorreo());

        usuario.getUsuarioPassword().setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(usuario);
        auditLogger.log("PASSWORD_RESET_BY_ADMIN", currentActor(), "targetId=%s".formatted(id));
    }

    private UsuarioDTO toDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        if (usuario.getUsuarioNombre() != null) {
            dto.setNombreCompleto(usuario.getUsuarioNombre().getNombreCompleto());
        }
        if (usuario.getUsuarioCorreo() != null) {
            dto.setCorreo(usuario.getUsuarioCorreo().getCorreo());
        }
        if (usuario.getUsuarioRol() != null) {
            dto.setRol(usuario.getUsuarioRol().getRol());
        }
        return dto;
    }

    private Usuario getCurrentUserEntity() {
        AuthenticatedUser currentUser = SecurityUtils.currentUser();
        return usuarioRepository.findDetailedById(currentUser.getId())
                .orElseThrow(() -> new BusinessException("error.usuario.noEncontrado", HttpStatus.NOT_FOUND));
    }

    private String currentActor() {
        return SecurityUtils.currentUsernameOrSystem();
    }
}
