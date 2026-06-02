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
 * Tabla satelite que conserva el idioma tecnico de una reserva.
 */
@Getter
@Setter
@Entity
@Table(name = "reservas_idioma")
public class ReservaIdioma {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Reserva reserva;

    @Column(name = "idioma", nullable = false, length = 10)
    private String idioma;
}
