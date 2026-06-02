package co.edu.usco.peerlink.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Clave compuesta para identificar de forma unica una relacion tutor-materia.
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TutorMateriaId implements Serializable {
    @Column(name = "tutor_id")
    private Integer tutorId;

    @Column(name = "materia_id")
    private Integer materiaId;
}
