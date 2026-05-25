package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.TutorMateria;
import co.edu.usco.peerlink.model.TutorMateriaId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TutorMateriaRepository extends JpaRepository<TutorMateria, TutorMateriaId> {

    @Query("""
            select distinct tm
            from TutorMateria tm
            join fetch tm.tutor t
            join fetch t.usuarioNombre un
            join fetch t.usuarioCorreo uc
            join fetch tm.materia m
            join fetch m.materiaIdioma mi
            join fetch m.materiaFacultad mf
            left join fetch tm.tutorMateriaFecha tmfecha
            order by m.nombre, un.nombreCompleto
            """)
    List<TutorMateria> findAllDetailed();

    @Query("""
            select distinct tm
            from TutorMateria tm
            join fetch tm.tutor t
            join fetch t.usuarioNombre un
            join fetch t.usuarioCorreo uc
            join fetch tm.materia m
            join fetch m.materiaIdioma mi
            join fetch m.materiaFacultad mf
            left join fetch tm.tutorMateriaFecha tmfecha
            where t.id = :tutorId
            order by m.nombre asc
            """)
    List<TutorMateria> findAllByTutorIdDetailed(Integer tutorId);

    @Modifying
    @Query("delete from TutorMateria tm where tm.tutor.id = :tutorId")
    void deleteAllByTutorId(Integer tutorId);
}
