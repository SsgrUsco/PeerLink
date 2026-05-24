package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.exception.BusinessException;
import co.edu.usco.peerlink.model.Materia;
import co.edu.usco.peerlink.model.TutorMateria;
import co.edu.usco.peerlink.model.TutorMateriaFacultad;
import co.edu.usco.peerlink.model.TutorMateriaId;
import co.edu.usco.peerlink.model.TutorMateriaIdioma;
import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.MateriaRepository;
import co.edu.usco.peerlink.repository.TutorMateriaRepository;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
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
        if (materiaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessException("materia.error.existe", HttpStatus.CONFLICT);
        }

        Materia materia = new Materia();
        materia.setNombre(nombre);
        return toDto(materiaRepository.save(materia));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> obtenerTodas() {
        return materiaRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public MateriaDTO actualizarMateria(Integer id, MateriaDTO dto) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("error.materia.noEncontrada", HttpStatus.NOT_FOUND));

        String nombre = dto.getNombre().trim();
        if (!materia.getNombre().equalsIgnoreCase(nombre) && materiaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessException("materia.error.existe", HttpStatus.CONFLICT);
        }

        materia.setNombre(nombre);
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

        TutorMateriaIdioma tutorMateriaIdioma = new TutorMateriaIdioma();
        tutorMateriaIdioma.setId(tutorMateriaId);
        tutorMateriaIdioma.setIdioma(dto.getIdioma());
        tutorMateriaIdioma.setTutorMateria(tm);
        tm.setTutorMateriaIdioma(tutorMateriaIdioma);

        TutorMateriaFacultad tutorMateriaFacultad = new TutorMateriaFacultad();
        tutorMateriaFacultad.setId(tutorMateriaId);
        tutorMateriaFacultad.setFacultad(dto.getFacultad());
        tutorMateriaFacultad.setTutorMateria(tm);
        tm.setTutorMateriaFacultad(tutorMateriaFacultad);

        tutorMateriaRepository.save(tm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorMateriaDetalleDTO> listarAsignacionesTutorMateria() {
        return tutorMateriaRepository.findAllDetailed().stream()
                .map(this::toDetalleDto)
                .toList();
    }

    private MateriaDTO toDto(Materia materia) {
        MateriaDTO dto = new MateriaDTO();
        dto.setId(materia.getId());
        dto.setNombre(materia.getNombre());
        return dto;
    }

    private TutorMateriaDetalleDTO toDetalleDto(TutorMateria tutorMateria) {
        TutorMateriaDetalleDTO dto = new TutorMateriaDetalleDTO();
        dto.setTutorId(tutorMateria.getTutor().getId());
        dto.setTutorNombre(tutorMateria.getTutor().getUsuarioNombre().getNombreCompleto());
        dto.setTutorCorreo(tutorMateria.getTutor().getUsuarioCorreo().getCorreo());
        dto.setMateriaId(tutorMateria.getMateria().getId());
        dto.setMateriaNombre(tutorMateria.getMateria().getNombre());
        dto.setIdioma(tutorMateria.getTutorMateriaIdioma().getIdioma());
        dto.setFacultad(tutorMateria.getTutorMateriaFacultad().getFacultad());
        return dto;
    }
}
