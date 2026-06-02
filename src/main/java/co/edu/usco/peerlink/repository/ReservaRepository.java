package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para reservas y sus tablas satelite en el modelo 6NF.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    /**
     * Obtiene todas las reservas con sus relaciones principales cargadas.
     *
     * @return reservas detalladas para reportes o administracion
     */
    @Query("""
            select distinct r
            from Reserva r
            join fetch r.reservaEstudiante re
            join fetch re.estudiante e
            join fetch r.reservaTutorMateria rtm
            join fetch rtm.tutorMateria tm
            join fetch tm.tutor t
            join fetch tm.materia m
            join fetch r.reservaFecha rf
            join fetch r.reservaEstado rs
            join fetch r.reservaIdioma ri
            join fetch r.reservaFacultad rfac
            """)
    List<Reserva> findAllDetailed();

    /**
     * Consulta reservas pertenecientes a un estudiante con filtros opcionales.
     *
     * @param estudianteId identificador del estudiante
     * @param idioma filtro por idioma o cadena vacia
     * @param facultad filtro por facultad o cadena vacia
     * @param desde fecha minima incluida
     * @param hasta fecha maxima excluida
     * @return reservas detalladas del estudiante
     */
    @Query("""
            select distinct r
            from Reserva r
            join fetch r.reservaEstudiante re
            join fetch re.estudiante e
            join fetch r.reservaTutorMateria rtm
            join fetch rtm.tutorMateria tm
            join fetch tm.tutor t
            join fetch tm.materia m
            join fetch r.reservaFecha rf
            join fetch r.reservaEstado rs
            join fetch r.reservaIdioma ri
            join fetch r.reservaFacultad rfac
            where e.id = :estudianteId
              and (:idioma = '' or ri.idioma = :idioma)
              and (:facultad = '' or rfac.facultad = :facultad)
              and rf.fechaHora >= :desde
              and rf.fechaHora < :hasta
            order by rf.fechaHora asc
            """)
    List<Reserva> findAllByEstudianteId(Integer estudianteId, String idioma, String facultad,
                                        LocalDateTime desde, LocalDateTime hasta);

    /**
     * Consulta reservas asociadas a un tutor con filtros opcionales.
     *
     * @param tutorId identificador del tutor
     * @param idioma filtro por idioma o cadena vacia
     * @param facultad filtro por facultad o cadena vacia
     * @param desde fecha minima incluida
     * @param hasta fecha maxima excluida
     * @return tutorias solicitadas o aceptadas por el tutor
     */
    @Query("""
            select distinct r
            from Reserva r
            join fetch r.reservaEstudiante re
            join fetch re.estudiante e
            join fetch r.reservaTutorMateria rtm
            join fetch rtm.tutorMateria tm
            join fetch tm.tutor t
            join fetch tm.materia m
            join fetch r.reservaFecha rf
            join fetch r.reservaEstado rs
            join fetch r.reservaIdioma ri
            join fetch r.reservaFacultad rfac
            where t.id = :tutorId
              and (:idioma = '' or ri.idioma = :idioma)
              and (:facultad = '' or rfac.facultad = :facultad)
              and rf.fechaHora >= :desde
              and rf.fechaHora < :hasta
            order by rf.fechaHora asc
            """)
    List<Reserva> findAllByTutorId(Integer tutorId, String idioma, String facultad,
                                   LocalDateTime desde, LocalDateTime hasta);

    /**
     * Indica si un estudiante tiene reservas asociadas.
     *
     * @param estudianteId identificador del estudiante
     * @return {@code true} cuando existen reservas relacionadas
     */
    @Query("""
            select count(r) > 0
            from Reserva r
            join r.reservaEstudiante re
            where re.estudiante.id = :estudianteId
            """)
    boolean existsByEstudianteId(Integer estudianteId);

    /**
     * Indica si un tutor tiene reservas asociadas.
     *
     * @param tutorId identificador del tutor
     * @return {@code true} cuando existen reservas relacionadas
     */
    @Query("""
            select count(r) > 0
            from Reserva r
            join r.reservaTutorMateria rtm
            join rtm.tutorMateria tm
            where tm.tutor.id = :tutorId
            """)
    boolean existsByTutorId(Integer tutorId);
}
