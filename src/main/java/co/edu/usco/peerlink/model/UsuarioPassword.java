package co.edu.usco.peerlink.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "usuarios_password")
public class UsuarioPassword {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Usuario usuario;

    @Column(name = "password", nullable = false)
    private String password;
}