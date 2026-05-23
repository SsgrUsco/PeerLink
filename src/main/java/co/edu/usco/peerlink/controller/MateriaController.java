package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDTO;
import co.edu.usco.peerlink.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity<MateriaDTO> crear(@RequestBody MateriaDTO dto) {
        return new ResponseEntity<>(materiaService.crearMateria(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MateriaDTO>> listar() {
        return ResponseEntity.ok(materiaService.obtenerTodas());
    }

    @PostMapping("/asignar-tutor")
    public ResponseEntity<String> asignarTutor(@RequestBody TutorMateriaDTO dto) {
        materiaService.asignarTutor(dto);
        return ResponseEntity.ok("Materia asignada al tutor de forma exitosa");
    }
}