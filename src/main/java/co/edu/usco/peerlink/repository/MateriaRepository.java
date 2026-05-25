package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    @Query("""
            select distinct m
            from Materia m
            join fetch m.materiaIdioma mi
            join fetch m.materiaFacultad mf
            order by m.nombre
            """)
    List<Materia> findAllDetailed();

    @Query("""
            select count(m) > 0
            from Materia m
            join m.materiaIdioma mi
            join m.materiaFacultad mf
            where lower(m.nombre) = lower(:nombre)
              and mi.idioma = :idioma
              and mf.facultad = :facultad
            """)
    boolean existsByNombreIdiomaFacultad(String nombre, String idioma, String facultad);
}
