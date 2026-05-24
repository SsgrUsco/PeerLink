package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.ReservaDetalleDTO;
import co.edu.usco.peerlink.dto.ReservaDTO;
import co.edu.usco.peerlink.dto.ReservaEstadoUpdateDTO;
import co.edu.usco.peerlink.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Operaciones para crear y gestionar reservas de tutorías.")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @Operation(
            summary = "Crear reserva",
            description = "Permite a un ESTUDIANTE crear una nueva reserva usando una combinación válida de tutor y materia. El estado inicial siempre es PENDIENTE."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida o relación tutor-materia inválida"),
            @ApiResponse(responseCode = "403", description = "Solo disponible para ESTUDIANTE")
    })
    public ResponseEntity<ReservaDTO> crear(@Valid @RequestBody ReservaDTO dto) {
        return new ResponseEntity<>(reservaService.crearReserva(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Listar reservas del usuario autenticado",
            description = "Devuelve reservas resumidas del usuario actual. Para estudiantes muestra sus reservas; para tutores, sus tutorías asignadas."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Listado de reservas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservaDTO.class))))
    public ResponseEntity<List<ReservaDTO>> listar() {
        return ResponseEntity.ok(reservaService.obtenerReservasDelUsuario());
    }

    @GetMapping("/mis-reservas")
    @Operation(
            summary = "Consultar mis reservas",
            description = "Vista detallada para ESTUDIANTE con materia, tutor, fecha y estado de cada solicitud."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado detallado de reservas del estudiante",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservaDetalleDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para ESTUDIANTE")
    })
    public ResponseEntity<List<ReservaDetalleDTO>> misReservas(
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) String facultad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return ResponseEntity.ok(reservaService.obtenerMisReservas(idioma, facultad, desde, hasta));
    }

    @GetMapping("/mis-tutorias")
    @Operation(
            summary = "Consultar mis tutorías",
            description = "Vista detallada para TUTOR con estudiante, materia, fecha y estado de cada solicitud recibida."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado detallado de tutorías del tutor",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservaDetalleDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR")
    })
    public ResponseEntity<List<ReservaDetalleDTO>> misTutorias(
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) String facultad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return ResponseEntity.ok(reservaService.obtenerMisTutorias(idioma, facultad, desde, hasta));
    }

    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Actualizar estado de reserva",
            description = "Permite a un TUTOR cambiar una reserva PENDIENTE a CONFIRMADA o CANCELADA, solo si la reserva le pertenece."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR dueño de la reserva"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDTO> actualizarEstado(@PathVariable Integer id,
                                                       @Valid @RequestBody ReservaEstadoUpdateDTO dto) {
        return ResponseEntity.ok(reservaService.actualizarEstado(id, dto.getEstado()));
    }
}
