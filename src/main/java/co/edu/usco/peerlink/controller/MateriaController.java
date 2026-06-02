package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.dto.TutorOfertaDTO;
import co.edu.usco.peerlink.service.MateriaService;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el catalogo de materias y publicaciones de tutores.
 *
 * <p>Permite a ADMIN administrar materias y asignaciones, y a TUTOR publicar
 * o retirar tutorias disponibles sobre materias existentes.</p>
 */
@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "Gestion de materias, ofertas de tutores y asignaciones tutor-materia.")
public class MateriaController {

    private final MateriaService materiaService;
    private final MessageSource messageSource;

    public MateriaController(MateriaService materiaService, MessageSource messageSource) {
        this.materiaService = materiaService;
        this.messageSource = messageSource;
    }

    @PostMapping
    @Operation(
            summary = "Crear materia",
            description = "Crea una nueva materia del catalogo. Requiere rol ADMIN. El idioma y la facultad se guardan como claves tecnicas estables para i18n manual."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Materia creada",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validacion fallida"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN"),
            @ApiResponse(responseCode = "409", description = "La materia ya existe")
    })
    public ResponseEntity<MateriaDTO> crear(@Valid @RequestBody MateriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.crearMateria(dto));
    }

    @GetMapping
    @Operation(
            summary = "Listar materias",
            description = "Devuelve todas las materias registradas con su idioma y facultad. Puede ser consumido por ADMIN, TUTOR y ESTUDIANTE autenticados."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Listado de materias",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MateriaDTO.class))))
    public ResponseEntity<List<MateriaDTO>> listar() {
        return ResponseEntity.ok(materiaService.obtenerTodas());
    }

    @GetMapping("/tutores-materias")
    @Operation(
            summary = "Listar relaciones tutor-materia",
            description = "Consulta las ofertas/asignaciones tutor-materia, incluyendo tutor, materia, idioma, facultad y fecha/hora publicada."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Listado de relaciones tutor-materia",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TutorMateriaDetalleDTO.class))))
    public ResponseEntity<List<TutorMateriaDetalleDTO>> listarAsignaciones() {
        return ResponseEntity.ok(materiaService.listarAsignacionesTutorMateria());
    }

    @GetMapping("/mis-materias")
    @Operation(
            summary = "Listar mis tutorias publicadas",
            description = "Permite al TUTOR consultar las materias/tutorias que ha publicado, con fecha/hora, idioma y facultad."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de tutorias publicadas por el tutor",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TutorMateriaDetalleDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR")
    })
    public ResponseEntity<List<TutorMateriaDetalleDTO>> listarMisMateriasTutor() {
        return ResponseEntity.ok(materiaService.listarMisMateriasTutor());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar materia",
            description = "Actualiza nombre, idioma y facultad de una materia existente. Requiere rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materia actualizada",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validacion fallida"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<MateriaDTO> actualizar(
            @Parameter(description = "ID de la materia a actualizar.", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody MateriaDTO dto) {
        return ResponseEntity.ok(materiaService.actualizarMateria(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar materia",
            description = "Elimina una materia por ID. Requiere rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Materia eliminada"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la materia a eliminar.", example = "1")
            @PathVariable Integer id) {
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/asignar-tutor")
    @Operation(
            summary = "Asignar tutor a materia",
            description = "Crea una relacion administrativa entre tutor y materia. El tutor debe existir y tener rol TUTOR."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tutor asignado a la materia"),
            @ApiResponse(responseCode = "400", description = "Tutor invalido o datos incorrectos"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN"),
            @ApiResponse(responseCode = "409", description = "La relacion ya existe")
    })
    public ResponseEntity<Map<String, String>> asignarTutor(@Valid @RequestBody TutorMateriaDTO dto) {
        materiaService.asignarTutor(dto);
        return ResponseEntity.ok(Map.of(
                "message",
                messageSource.getMessage("materia.tutor.asignado", null, LocaleContextHolder.getLocale())
        ));
    }

    @PostMapping("/mis-materias")
    @Operation(
            summary = "Crear tutoria como tutor",
            description = "Permite al TUTOR publicar una tutoria seleccionando una materia existente y una fecha/hora futura. El idioma y la facultad se toman de la materia seleccionada."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tutoria publicada"),
            @ApiResponse(responseCode = "400", description = "Validacion fallida o materia invalida"),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR")
    })
    public ResponseEntity<Map<String, String>> crearOfertaTutor(@Valid @RequestBody TutorOfertaDTO dto) {
        materiaService.crearOfertaTutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message",
                messageSource.getMessage("materia.tutor.asignado", null, LocaleContextHolder.getLocale())
        ));
    }

    @DeleteMapping("/mis-materias/{materiaId}")
    @Operation(
            summary = "Eliminar tutoria publicada",
            description = "Permite al TUTOR retirar una tutoria/materia publicada de su propio horario."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tutoria retirada"),
            @ApiResponse(responseCode = "403", description = "Solo disponible para TUTOR"),
            @ApiResponse(responseCode = "404", description = "Tutoria no encontrada")
    })
    public ResponseEntity<Void> eliminarOfertaTutor(
            @Parameter(description = "ID de la materia publicada por el tutor.", example = "1")
            @PathVariable Integer materiaId) {
        materiaService.eliminarOfertaTutor(materiaId);
        return ResponseEntity.noContent().build();
    }
}
