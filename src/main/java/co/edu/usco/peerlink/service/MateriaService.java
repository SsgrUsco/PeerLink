package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.dto.TutorOfertaDTO;

import java.util.List;

/**
 * Contrato de negocio para materias y tutorias publicadas.
 *
 * <p>Administra el catalogo creado por administracion y las relaciones
 * tutor-materia usadas por estudiantes para solicitar o aceptar tutorias.
 * Idioma y facultad se manejan como claves tecnicas para mantener i18n manual.</p>
 */
public interface MateriaService {
    /**
     * Crea una materia del catalogo academico.
     *
     * @param dto datos de la materia, idioma y facultad
     * @return materia creada
     */
    MateriaDTO crearMateria(MateriaDTO dto);

    /**
     * Lista todas las materias disponibles en el catalogo.
     *
     * @return materias registradas
     */
    List<MateriaDTO> obtenerTodas();

    /**
     * Actualiza una materia existente.
     *
     * @param id identificador de la materia
     * @param dto nuevos datos
     * @return materia actualizada
     */
    MateriaDTO actualizarMateria(Integer id, MateriaDTO dto);

    /**
     * Elimina una materia del catalogo si las reglas de negocio lo permiten.
     *
     * @param id identificador de la materia
     */
    void eliminarMateria(Integer id);

    /**
     * Asocia administrativamente un tutor con una materia existente.
     *
     * @param dto identificadores de tutor y materia
     */
    void asignarTutor(TutorMateriaDTO dto);

    /**
     * Lista relaciones tutor-materia con detalles visibles para administracion.
     *
     * @return asignaciones y tutorias publicadas
     */
    List<TutorMateriaDetalleDTO> listarAsignacionesTutorMateria();

    /**
     * Lista las tutorias publicadas por el tutor autenticado.
     *
     * @return tutorias propias del tutor
     */
    List<TutorMateriaDetalleDTO> listarMisMateriasTutor();

    /**
     * Publica una tutoria para el tutor autenticado en una fecha y hora futura.
     *
     * @param dto materia y fecha/hora seleccionada
     */
    void crearOfertaTutor(TutorOfertaDTO dto);

    /**
     * Retira una tutoria publicada por el tutor autenticado.
     *
     * @param materiaId identificador de la materia publicada
     */
    void eliminarOfertaTutor(Integer materiaId);
}
