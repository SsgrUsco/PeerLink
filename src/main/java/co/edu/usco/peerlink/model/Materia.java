package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;

    @OneToOne(mappedBy = "materia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private MateriaIdioma materiaIdioma;

    @OneToOne(mappedBy = "materia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private MateriaFacultad materiaFacultad;
}
