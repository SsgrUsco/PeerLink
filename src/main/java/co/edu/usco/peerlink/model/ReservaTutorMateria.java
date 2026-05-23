package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "reservas_tutor_materia")
public class ReservaTutorMateria {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Reserva reserva;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "tutor_id", referencedColumnName = "tutor_id"),
            @JoinColumn(name = "materia_id", referencedColumnName = "materia_id")
    })
    private TutorMateria tutorMateria;
}