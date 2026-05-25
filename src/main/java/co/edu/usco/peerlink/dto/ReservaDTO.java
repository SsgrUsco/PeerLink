package co.edu.usco.peerlink.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDTO {
    private Integer id;
    private Integer estudianteId;

    private Integer tutorId;

    private Integer materiaId;

    private LocalDateTime fechaHora;

    private String idioma;

    private String facultad;

    private String estado;
}
