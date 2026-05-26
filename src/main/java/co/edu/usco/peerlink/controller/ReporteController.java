package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Generacion de reportes PDF con JasperReports Library.")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/mis-reservas.pdf")
    @Operation(
            summary = "Descargar reporte PDF de reservas del estudiante",
            description = "Genera un PDF con las reservas del ESTUDIANTE autenticado. Permite filtrar por idioma, facultad y rango de fecha/hora."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte PDF generado",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para ESTUDIANTE")
    })
    public ResponseEntity<byte[]> misReservas(
            @Parameter(description = "Filtro opcional por idioma tecnico.", example = "es", schema = @Schema(allowableValues = {"es", "en", "pt"}))
            @RequestParam(required = false) String idioma,
            @Parameter(description = "Filtro opcional por clave tecnica de facultad.", example = "INGENIERIA")
            @RequestParam(required = false) String facultad,
            @Parameter(description = "Inicio opcional del rango de reporte en formato ISO.", example = "2026-06-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fin opcional del rango de reporte en formato ISO.", example = "2026-06-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return pdf("peerlink-mis-reservas.pdf", reporteService.generarReporteMisReservas(idioma, facultad, desde, hasta));
    }

    @GetMapping("/mis-tutorias.pdf")
    @Operation(
            summary = "Descargar reporte PDF de tutorias del tutor",
            description = "Genera un PDF con las tutorias/solicitudes del TUTOR autenticado. Permite filtrar por idioma, facultad y rango de fecha/hora."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte PDF generado",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR")
    })
    public ResponseEntity<byte[]> misTutorias(
            @Parameter(description = "Filtro opcional por idioma tecnico.", example = "es", schema = @Schema(allowableValues = {"es", "en", "pt"}))
            @RequestParam(required = false) String idioma,
            @Parameter(description = "Filtro opcional por clave tecnica de facultad.", example = "INGENIERIA")
            @RequestParam(required = false) String facultad,
            @Parameter(description = "Inicio opcional del rango de reporte en formato ISO.", example = "2026-06-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fin opcional del rango de reporte en formato ISO.", example = "2026-06-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return pdf("peerlink-mis-tutorias.pdf", reporteService.generarReporteMisTutorias(idioma, facultad, desde, hasta));
    }

    @GetMapping("/admin/resumen.pdf")
    @Operation(
            summary = "Descargar reporte PDF administrativo",
            description = "Genera un resumen administrativo global. Requiere rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte PDF generado",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<byte[]> adminResumen() {
        return pdf("peerlink-admin-resumen.pdf", reporteService.generarReporteAdminResumen());
    }

    @GetMapping("/admin/tutores.pdf")
    @Operation(
            summary = "Descargar reporte PDF administrativo de tutores",
            description = "Genera un reporte administrativo de tutores, materias asignadas y actividad relacionada. Requiere rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte PDF generado",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<byte[]> adminTutores() {
        return pdf("peerlink-admin-tutores.pdf", reporteService.generarReporteAdminTutores());
    }

    @GetMapping("/admin/usuarios.pdf")
    @Operation(
            summary = "Descargar reporte PDF administrativo de usuarios",
            description = "Genera un reporte administrativo de usuarios registrados y roles. Requiere rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte PDF generado",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<byte[]> adminUsuarios() {
        return pdf("peerlink-admin-usuarios.pdf", reporteService.generarReporteAdminUsuarios());
    }

    private ResponseEntity<byte[]> pdf(String filename, byte[] content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentLength(content.length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }
}
