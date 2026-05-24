package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

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

    @Query("""
            select count(r) > 0
            from Reserva r
            join r.reservaEstudiante re
            where re.estudiante.id = :estudianteId
            """)
    boolean existsByEstudianteId(Integer estudianteId);

    @Query("""
            select count(r) > 0
            from Reserva r
            join r.reservaTutorMateria rtm
            join rtm.tutorMateria tm
            where tm.tutor.id = :tutorId
            """)
    boolean existsByTutorId(Integer tutorId);
}
