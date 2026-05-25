package co.edu.usco.peerlink.dto;

import lombok.Data;

@Data
public class TutorMateriaDetalleDTO {
    private Integer tutorId;
    private String tutorNombre;
    private String tutorCorreo;
    private Integer materiaId;
    private String materiaNombre;
    private java.time.LocalDateTime fechaHora;
    private String idioma;
    private String facultad;
}
