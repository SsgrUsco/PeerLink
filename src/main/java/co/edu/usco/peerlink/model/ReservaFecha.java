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
import java.time.LocalDateTime;

/**
 * Tabla satelite que guarda la fecha y hora programada de la reserva.
 */
@Getter
@Setter
@Entity
@Table(name = "reservas_fecha")
public class ReservaFecha {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Reserva reserva;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
}
