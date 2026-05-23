package co.edu.usco.peerlink.repository;
import co.edu.usco.peerlink.model.TutorMateria;
import co.edu.usco.peerlink.model.TutorMateriaId;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TutorMateriaRepository extends JpaRepository<TutorMateria, TutorMateriaId> {}