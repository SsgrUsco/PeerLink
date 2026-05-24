package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);
}
