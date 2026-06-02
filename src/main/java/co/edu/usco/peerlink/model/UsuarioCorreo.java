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
 * Tabla satelite que almacena el correo unico de un usuario.
 */
@Getter
@Setter
@Entity
@Table(name = "usuarios_correo")
public class UsuarioCorreo {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Usuario usuario;

    @Column(name = "correo", unique = true, nullable = false)
    private String correo;
}
