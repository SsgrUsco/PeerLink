package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;

import java.util.List;

public interface MateriaService {
    MateriaDTO crearMateria(MateriaDTO dto);
    List<MateriaDTO> obtenerTodas();
    MateriaDTO actualizarMateria(Integer id, MateriaDTO dto);
    void eliminarMateria(Integer id);
    void asignarTutor(TutorMateriaDTO dto);
    List<TutorMateriaDetalleDTO> listarAsignacionesTutorMateria();
}
