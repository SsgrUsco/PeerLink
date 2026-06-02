package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.ReservaDetalleDTO;
import co.edu.usco.peerlink.dto.ReservaDTO;
import co.edu.usco.peerlink.dto.ReservaEstadoUpdateDTO;
import co.edu.usco.peerlink.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * Controlador REST para reservas y solicitudes de tutorias.
 *
 * <p>Expone operaciones para crear reservas, consultar horarios filtrados y
 * permitir que el tutor confirme, cancele o deshaga respuestas segun la regla
 * de negocio vigente.</p>
 */
@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Gestion de reservas, solicitudes, filtros y horarios semanales.")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @Operation(
            summary = "Crear reserva",
            description = "Permite a un ESTUDIANTE crear una nueva reserva usando una combinacion valida de tutor y materia. El estado inicial siempre es PENDIENTE. Incluye idioma, facultad y fecha/hora."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validacion fallida o relacion tutor-materia invalida"),
            @ApiResponse(responseCode = "403", description = "Solo disponible para ESTUDIANTE")
    })
    public ResponseEntity<ReservaDTO> crear(@Valid @RequestBody ReservaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crearReserva(dto));
    }

    @GetMapping
    @Operation(
            summary = "Listar reservas del usuario autenticado",
            description = "Devuelve reservas resumidas del usuario actual. Para estudiantes muestra sus reservas; para tutores, sus tutorias asignadas."
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
            description = "Vista detallada para ESTUDIANTE con materia, tutor, idioma, facultad, fecha/hora y estado. Los filtros permiten alimentar la lista y el horario semanal."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado detallado de reservas del estudiante",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservaDetalleDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para ESTUDIANTE")
    })
    public ResponseEntity<List<ReservaDetalleDTO>> misReservas(
            @Parameter(description = "Filtro opcional por idioma tecnico.", example = "es", schema = @Schema(allowableValues = {"es", "en", "pt"}))
            @RequestParam(required = false) String idioma,
            @Parameter(description = "Filtro opcional por clave tecnica de facultad.", example = "INGENIERIA")
            @RequestParam(required = false) String facultad,
            @Parameter(description = "Inicio opcional del rango de busqueda en formato ISO.", example = "2026-06-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fin opcional del rango de busqueda en formato ISO.", example = "2026-06-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return ResponseEntity.ok(reservaService.obtenerMisReservas(idioma, facultad, desde, hasta));
    }

    @GetMapping("/mis-tutorias")
    @Operation(
            summary = "Consultar mis tutorias",
            description = "Vista detallada para TUTOR con estudiante, materia, idioma, facultad, fecha/hora y estado de cada solicitud recibida. Los filtros permiten alimentar la lista y el horario semanal."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado detallado de tutorias del tutor",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservaDetalleDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR")
    })
    public ResponseEntity<List<ReservaDetalleDTO>> misTutorias(
            @Parameter(description = "Filtro opcional por idioma tecnico.", example = "es", schema = @Schema(allowableValues = {"es", "en", "pt"}))
            @RequestParam(required = false) String idioma,
            @Parameter(description = "Filtro opcional por clave tecnica de facultad.", example = "INGENIERIA")
            @RequestParam(required = false) String facultad,
            @Parameter(description = "Inicio opcional del rango de busqueda en formato ISO.", example = "2026-06-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fin opcional del rango de busqueda en formato ISO.", example = "2026-06-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return ResponseEntity.ok(reservaService.obtenerMisTutorias(idioma, facultad, desde, hasta));
    }

    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Actualizar estado de reserva",
            description = "Permite a un TUTOR cambiar una reserva PENDIENTE a CONFIRMADA o CANCELADA, solo si la reserva le pertenece. Tambien se utiliza para deshacer respuestas cuando se restaura el estado permitido por la logica de negocio."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Estado invalido"),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR dueno de la reserva"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDTO> actualizarEstado(
            @Parameter(description = "ID de la reserva a actualizar.", example = "10")
            @PathVariable Integer id,
            @Valid @RequestBody ReservaEstadoUpdateDTO dto) {
        return ResponseEntity.ok(reservaService.actualizarEstado(id, dto.getEstado()));
    }
}
