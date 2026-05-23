package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.model.Materia;
import co.edu.usco.peerlink.model.TutorMateria;
import co.edu.usco.peerlink.model.Usuario;
import co.edu.usco.peerlink.repository.MateriaRepository;
import co.edu.usco.peerlink.repository.TutorMateriaRepository;
import co.edu.usco.peerlink.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TutorMateriaRepository tutorMateriaRepository;

    @Override
    public MateriaDTO crearMateria(MateriaDTO dto) {
        Materia m = new Materia();
        m.setNombre(dto.getNombre());
        m = materiaRepository.save(m);
        dto.setId(m.getId());
        return dto;
    }

    @Override
    public List<MateriaDTO> obtenerTodas() {
        return materiaRepository.findAll().stream().map(m -> {
            MateriaDTO dto = new MateriaDTO();
            dto.setId(m.getId());
            dto.setNombre(m.getNombre());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void asignarTutor(TutorMateriaDTO dto) {
        Usuario tutor = usuarioRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        TutorMateria tm = new TutorMateria();
        tm.setTutor(tutor);
        tm.setMateria(materia);
        tutorMateriaRepository.save(tm);
    }
}