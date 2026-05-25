package co.edu.usco.peerlink.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TutorMateriaRepository tutorMateriaRepository;
    private final ReservaRepository reservaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              TutorMateriaRepository tutorMateriaRepository,
                              ReservaRepository reservaRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tutorMateriaRepository = tutorMateriaRepository;
        this.reservaRepository = reservaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioRegistroDTO dto) {
        return crearUsuario(dto.getNombreCompleto(), dto.getCorreo(), dto.getPassword(), dto.getRol());
    }

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioGestionDTO dto) {
        return crearUsuario(dto.getNombreCompleto(), dto.getCorreo(), dto.getPassword(), dto.getRol());
    }

    private UsuarioDTO crearUsuario(String nombreCompleto, String correoValue, String passwordValue, String rolValue) {
        if (usuarioRepository.existsByUsuarioCorreoCorreoIgnoreCase(correoValue)) {
            throw new BusinessException("error.usuario.correoDuplicado", HttpStatus.CONFLICT);
        }

        Usuario usuario = new Usuario();

        UsuarioNombre nombre = new UsuarioNombre();
        nombre.setNombreCompleto(nombreCompleto.trim());
        nombre.setUsuario(usuario);
        usuario.setUsuarioNombre(nombre);

        UsuarioCorreo correo = new UsuarioCorreo();
        correo.setCorreo(correoValue.trim().toLowerCase());
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

        return toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
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
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPerfilActual() {
        return toDto(getCurrentUserEntity());
    }

    @Override
    @Transactional
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
    public void actualizarPasswordActual(UsuarioPasswordUpdateDTO dto) {
        Usuario usuario = getCurrentUserEntity();
        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getUsuarioPassword().getPassword())) {
            throw new BusinessException("error.usuario.passwordActualInvalida", HttpStatus.BAD_REQUEST);
        }

        usuario.getUsuarioPassword().setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(usuario);
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
        AuthenticatedUser currentUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findDetailedById(currentUser.getId())
                .orElseThrow(() -> new BusinessException("error.usuario.noEncontrado", HttpStatus.NOT_FOUND));
    }
}
