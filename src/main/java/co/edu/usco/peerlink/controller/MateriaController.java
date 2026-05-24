package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "Gestión de materias y asignaciones entre tutores y materias.")
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
            description = "Crea una nueva materia. Requiere rol ADMIN. El nombre no puede estar en blanco ni repetido."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Materia creada",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "409", description = "La materia ya existe")
    })
    public ResponseEntity<MateriaDTO> crear(@Valid @RequestBody MateriaDTO dto) {
        return new ResponseEntity<>(materiaService.crearMateria(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Listar materias",
            description = "Devuelve todas las materias registradas. Puede ser consumido por ADMIN, TUTOR y ESTUDIANTE autenticados."
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
            description = "Consulta la tabla puente tutores_materias para identificar qué tutorías pueden reservarse para cada materia."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Listado de relaciones tutor-materia",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TutorMateriaDetalleDTO.class))))
    public ResponseEntity<List<TutorMateriaDetalleDTO>> listarAsignaciones() {
        return ResponseEntity.ok(materiaService.listarAsignacionesTutorMateria());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar materia",
            description = "Actualiza el nombre de una materia existente. Requiere rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materia actualizada",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<MateriaDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody MateriaDTO dto) {
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
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/asignar-tutor")
    @Operation(
            summary = "Asignar tutor a materia",
            description = "Crea una relación en la tabla puente tutores_materias. El tutor debe existir y tener rol TUTOR."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tutor asignado a la materia"),
            @ApiResponse(responseCode = "400", description = "Tutor inválido o datos incorrectos"),
            @ApiResponse(responseCode = "409", description = "La relación ya existe")
    })
    public ResponseEntity<Map<String, String>> asignarTutor(@Valid @RequestBody TutorMateriaDTO dto) {
        materiaService.asignarTutor(dto);
        return ResponseEntity.ok(Map.of(
                "message",
                messageSource.getMessage("materia.tutor.asignado", null, LocaleContextHolder.getLocale())
        ));
    }
}
