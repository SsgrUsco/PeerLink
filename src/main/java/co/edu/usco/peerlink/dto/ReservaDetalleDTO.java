package co.edu.usco.peerlink.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDetalleDTO {
    private Integer id;
    private Integer estudianteId;
    private String estudianteNombre;
    private Integer tutorId;
    private String tutorNombre;
    private Integer materiaId;
    private String materiaNombre;
    private LocalDateTime fechaHora;
    private String idioma;
    private String facultad;
    private String estado;
}
