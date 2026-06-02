package co.edu.usco.peerlink.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tabla satelite que enlaza una reserva con la combinacion tutor-materia.
 */
@Getter
@Setter
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
