package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
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