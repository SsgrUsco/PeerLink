package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.dto.TutorOfertaDTO;
import co.edu.usco.peerlink.exception.BusinessException;
import co.edu.usco.peerlink.model.Materia;
import co.edu.usco.peerlink.model.MateriaFacultad;
import co.edu.usco.peerlink.model.MateriaIdioma;
import co.edu.usco.peerlink.model.TutorMateria;
import co.edu.usco.peerlink.model.TutorMateriaId;
import co.edu.usco.peerlink.model.TutorMateriaFecha;
import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.MateriaRepository;
import co.edu.usco.peerlink.repository.TutorMateriaRepository;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import co.edu.usco.peerlink.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorMateriaRepository tutorMateriaRepository;

    public MateriaServiceImpl(MateriaRepository materiaRepository,
                              UsuarioRepository usuarioRepository,
                              TutorMateriaRepository tutorMateriaRepository) {
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorMateriaRepository = tutorMateriaRepository;
    }

    @Override
    @Transactional
    public MateriaDTO crearMateria(MateriaDTO dto) {
        String nombre = dto.getNombre().trim();
        if (materiaRepository.existsByNombreIdiomaFacultad(nombre, dto.getIdioma(), dto.getFacultad())) {
            throw new BusinessException("materia.error.existe", HttpStatus.CONFLICT);
        }

        Materia materia = new Materia();
        materia.setNombre(nombre);

        MateriaIdioma materiaIdioma = new MateriaIdioma();
        materiaIdioma.setIdioma(dto.getIdioma());
        materiaIdioma.setMateria(materia);
        materia.setMateriaIdioma(materiaIdioma);

        MateriaFacultad materiaFacultad = new MateriaFacultad();
        materiaFacultad.setFacultad(dto.getFacultad());
        materiaFacultad.setMateria(materia);
        materia.setMateriaFacultad(materiaFacultad);

        return toDto(materiaRepository.save(materia));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> obtenerTodas() {
        return materiaRepository.findAllDetailed().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public MateriaDTO actualizarMateria(Integer id, MateriaDTO dto) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("error.materia.noEncontrada", HttpStatus.NOT_FOUND));

        String nombre = dto.getNombre().trim();
        boolean sameTriple = materia.getNombre().equalsIgnoreCase(nombre)
                && materia.getMateriaIdioma().getIdioma().equals(dto.getIdioma())
                && materia.getMateriaFacultad().getFacultad().equals(dto.getFacultad());
        if (!sameTriple && materiaRepository.existsByNombreIdiomaFacultad(nombre, dto.getIdioma(), dto.getFacultad())) {
            throw new BusinessException("materia.error.existe", HttpStatus.CONFLICT);
        }

        materia.setNombre(nombre);
        materia.getMateriaIdioma().setIdioma(dto.getIdioma());
        materia.getMateriaFacultad().setFacultad(dto.getFacultad());
        return toDto(materiaRepository.save(materia));
    }

    @Override
    @Transactional
    public void eliminarMateria(Integer id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("error.materia.noEncontrada", HttpStatus.NOT_FOUND));
        materiaRepository.delete(materia);
    }

    @Override
    @Transactional
    public void asignarTutor(TutorMateriaDTO dto) {
        Usuario tutor = usuarioRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new BusinessException("error.tutor.noEncontrado", HttpStatus.NOT_FOUND));
        if (tutor.getUsuarioRol() == null || !"TUTOR".equalsIgnoreCase(tutor.getUsuarioRol().getRol())) {
            throw new BusinessException("error.tutor.rolInvalido", HttpStatus.BAD_REQUEST);
        }

        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new BusinessException("error.materia.noEncontrada", HttpStatus.NOT_FOUND));

        TutorMateriaId tutorMateriaId = new TutorMateriaId(dto.getTutorId(), dto.getMateriaId());
        if (tutorMateriaRepository.existsById(tutorMateriaId)) {
            throw new BusinessException("error.tutorMateria.duplicada", HttpStatus.CONFLICT);
        }

        TutorMateria tm = new TutorMateria();
        tm.setTutor(tutor);
        tm.setMateria(materia);
        tutorMateriaRepository.save(tm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorMateriaDetalleDTO> listarAsignacionesTutorMateria() {
        return tutorMateriaRepository.findAllDetailed().stream()
                .map(this::toDetalleDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorMateriaDetalleDTO> listarMisMateriasTutor() {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"TUTOR".equals(currentUser.getRol())) {
            throw new BusinessException("error.tutor.rolInvalido", HttpStatus.FORBIDDEN);
        }

        return tutorMateriaRepository.findAllByTutorIdDetailed(currentUser.getId()).stream()
                .map(this::toDetalleDto)
                .toList();
    }

    @Override
    @Transactional
    public void crearOfertaTutor(TutorOfertaDTO dto) {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"TUTOR".equals(currentUser.getRol())) {
            throw new BusinessException("error.tutor.rolInvalido", HttpStatus.FORBIDDEN);
        }

        Usuario tutor = usuarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new BusinessException("error.tutor.noEncontrado", HttpStatus.NOT_FOUND));

        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new BusinessException("error.materia.noEncontrada", HttpStatus.NOT_FOUND));

        TutorMateriaId tutorMateriaId = new TutorMateriaId(tutor.getId(), materia.getId());
        TutorMateria tutorMateria = tutorMateriaRepository.findById(tutorMateriaId).orElseGet(() -> {
            TutorMateria nuevaRelacion = new TutorMateria();
            nuevaRelacion.setTutor(tutor);
            nuevaRelacion.setMateria(materia);
            return nuevaRelacion;
        });

        TutorMateriaFecha tutorMateriaFecha = tutorMateria.getTutorMateriaFecha();
        if (tutorMateriaFecha == null) {
            tutorMateriaFecha = new TutorMateriaFecha();
            tutorMateriaFecha.setId(tutorMateriaId);
            tutorMateriaFecha.setTutorMateria(tutorMateria);
            tutorMateria.setTutorMateriaFecha(tutorMateriaFecha);
        }
        tutorMateriaFecha.setFechaHora(dto.getFechaHora());

        tutorMateriaRepository.save(tutorMateria);
    }

    @Override
    @Transactional
    public void eliminarOfertaTutor(Integer materiaId) {
        AuthenticatedUser currentUser = getCurrentUser();
        if (!"TUTOR".equals(currentUser.getRol())) {
            throw new BusinessException("error.tutor.rolInvalido", HttpStatus.FORBIDDEN);
        }

        TutorMateriaId id = new TutorMateriaId(currentUser.getId(), materiaId);
        TutorMateria tutorMateria = tutorMateriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("error.reserva.tutorMateriaInvalida", HttpStatus.NOT_FOUND));
        tutorMateriaRepository.delete(tutorMateria);
    }

    private MateriaDTO toDto(Materia materia) {
        MateriaDTO dto = new MateriaDTO();
        dto.setId(materia.getId());
        dto.setNombre(materia.getNombre());
        dto.setIdioma(materia.getMateriaIdioma().getIdioma());
        dto.setFacultad(materia.getMateriaFacultad().getFacultad());
        return dto;
    }

    private TutorMateriaDetalleDTO toDetalleDto(TutorMateria tutorMateria) {
        TutorMateriaDetalleDTO dto = new TutorMateriaDetalleDTO();
        dto.setTutorId(tutorMateria.getTutor().getId());
        dto.setTutorNombre(tutorMateria.getTutor().getUsuarioNombre().getNombreCompleto());
        dto.setTutorCorreo(tutorMateria.getTutor().getUsuarioCorreo().getCorreo());
        dto.setMateriaId(tutorMateria.getMateria().getId());
        dto.setMateriaNombre(tutorMateria.getMateria().getNombre());
        dto.setFechaHora(tutorMateria.getTutorMateriaFecha() == null ? null : tutorMateria.getTutorMateriaFecha().getFechaHora());
        dto.setIdioma(tutorMateria.getMateria().getMateriaIdioma().getIdioma());
        dto.setFacultad(tutorMateria.getMateria().getMateriaFacultad().getFacultad());
        return dto;
    }

    private AuthenticatedUser getCurrentUser() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
