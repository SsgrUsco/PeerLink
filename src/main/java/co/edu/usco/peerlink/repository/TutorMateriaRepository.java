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
            join fetch tm.tutorMateriaIdioma tmi
            join fetch tm.tutorMateriaFacultad tmf
            order by m.nombre, un.nombreCompleto
            """)
    List<TutorMateria> findAllDetailed();

    @Modifying
    @Query("delete from TutorMateria tm where tm.tutor.id = :tutorId")
    void deleteAllByTutorId(Integer tutorId);
}
