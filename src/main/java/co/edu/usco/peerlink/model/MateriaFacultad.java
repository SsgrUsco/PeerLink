package co.edu.usco.peerlink.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "materias_facultad")
public class MateriaFacultad {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Materia materia;

    @Column(name = "facultad", nullable = false, length = 100)
    private String facultad;
}
