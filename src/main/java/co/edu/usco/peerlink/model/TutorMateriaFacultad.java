package co.edu.usco.peerlink.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "tutores_materias_facultad")
public class TutorMateriaFacultad {

    @EmbeddedId
    private TutorMateriaId id;

    @OneToOne
    @JoinColumns({
            @jakarta.persistence.JoinColumn(name = "tutor_id", referencedColumnName = "tutor_id", insertable = false, updatable = false),
            @jakarta.persistence.JoinColumn(name = "materia_id", referencedColumnName = "materia_id", insertable = false, updatable = false)
    })
    @ToString.Exclude
    private TutorMateria tutorMateria;

    @Column(name = "facultad", nullable = false, length = 100)
    private String facultad;
}
