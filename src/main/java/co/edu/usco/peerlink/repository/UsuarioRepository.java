package co.edu.usco.peerlink.repository;
import co.edu.usco.peerlink.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {}