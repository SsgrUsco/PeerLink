package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.ReservaDTO;
import co.edu.usco.peerlink.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDTO> crear(@RequestBody ReservaDTO dto) {
        return new ResponseEntity<>(reservaService.crearReserva(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listar() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }
}