package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "materias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
}