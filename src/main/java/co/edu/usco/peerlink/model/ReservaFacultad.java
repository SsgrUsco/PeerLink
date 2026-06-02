package co.edu.usco.peerlink.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tabla satelite que conserva la facultad tecnica de una reserva.
 */
@Getter
@Setter
@Entity
@Table(name = "reservas_facultad")
public class ReservaFacultad {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Reserva reserva;

    @Column(name = "facultad", nullable = false, length = 100)
    private String facultad;
}
