package co.edu.usco.peerlink.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad puente que representa la relacion entre un tutor y una materia.
 */
@Getter
@Setter
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
