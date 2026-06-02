package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositorio JPA para consultar y persistir materias del catalogo academico.
 */
public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    /**
     * Obtiene las materias con idioma y facultad cargados para evitar consultas adicionales.
     *
     * @return materias ordenadas por nombre
     */
    @Query("""
            select distinct m
            from Materia m
            join fetch m.materiaIdioma mi
            join fetch m.materiaFacultad mf
            order by m.nombre
            """)
    List<Materia> findAllDetailed();

    /**
     * Verifica si ya existe una materia con la misma combinacion de nombre, idioma y facultad.
     *
     * @param nombre nombre de la materia
     * @param idioma clave tecnica del idioma
     * @param facultad clave tecnica de la facultad
     * @return {@code true} si la combinacion ya existe
     */
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
