package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tutores_materias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorMateria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Usuario tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
}