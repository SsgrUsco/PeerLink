package co.edu.usco.peerlink.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tabla satelite que vincula una reserva con el estudiante solicitante.
 */
@Getter
@Setter
@Entity
@Table(name = "reservas_estudiante")
public class ReservaEstudiante {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;
}
