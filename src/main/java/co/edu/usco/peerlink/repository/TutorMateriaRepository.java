package co.edu.usco.peerlink.repository;

import co.edu.usco.peerlink.model.TutorMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TutorMateriaRepository extends JpaRepository<TutorMateria, Long> {
    List<TutorMateria> findByTutorId(Long tutorId);
    List<TutorMateria> findByMateriaId(Long materiaId);
}