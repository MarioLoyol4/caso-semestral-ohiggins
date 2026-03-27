package fullstack.caso_semestral.academic.controllers;


import fullstack.caso_semestral.academic.models.Asignatura;
import fullstack.caso_semestral.academic.services.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {

    // Conecta automaticamente con AsignaturaService
    @Autowired
    private AsignaturaService asignaturaService;

    // Se usa para pedir datos (leer)
    @GetMapping
    public ResponseEntity<List<Asignatura>> obtenerTodas() {
        List<Asignatura> asignaturas = asignaturaService.obtenerTodasLasAsignaturas();
        // aqui retorna las asignaturas y manda respuesta que se completo correctamente
        return new ResponseEntity<>(asignaturas, HttpStatus.OK);
    }

    // Se usa para enviar datos nuevos (crear)
    @PostMapping
    // RequestBody tooma el texto en JSON y lo transforma a un objeto java (en este caso asignatura)
    public ResponseEntity<Asignatura> crearAsignatura(@RequestBody Asignatura asignatura) {
        Asignatura nuevaAsignatura = asignaturaService.guardarAsignatura(asignatura);
        // se crea la nueva asignatura y se manda respuesta de que se creo
        return  new ResponseEntity<>(nuevaAsignatura, HttpStatus.CREATED);
    }
}
