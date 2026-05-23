package co.edu.usco.peerlink.service;
import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import java.util.List;

public interface MateriaService {
    MateriaDTO crearMateria(MateriaDTO dto);
    List<MateriaDTO> obtenerTodas();
    void asignarTutor(TutorMateriaDTO dto); // ¡Importante para conectar tutores con materias!
}