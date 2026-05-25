package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tutores_materias")
public class TutorMateria {

    @EmbeddedId
    private TutorMateriaId id = new TutorMateriaId();

    @ManyToOne
    @MapsId("tutorId")
    @JoinColumn(name = "tutor_id")
    private Usuario tutor;

    @ManyToOne
    @MapsId("materiaId")
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @OneToOne(mappedBy = "tutorMateria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private TutorMateriaIdioma tutorMateriaIdioma;

    @OneToOne(mappedBy = "tutorMateria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private TutorMateriaFacultad tutorMateriaFacultad;

    @OneToOne(mappedBy = "tutorMateria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private TutorMateriaFecha tutorMateriaFecha;
}
