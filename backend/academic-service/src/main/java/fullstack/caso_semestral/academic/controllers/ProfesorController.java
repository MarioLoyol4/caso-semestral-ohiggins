package fullstack.caso_semestral.academic.controllers;

import fullstack.caso_semestral.academic.models.Profesor;
import fullstack.caso_semestral.academic.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    // Conecta automaticamente con ProfesorService
    @Autowired
    private ProfesorService profesorService;

    // Se usa para pedir datos (leer)
    @GetMapping
    public ResponseEntity<List<Profesor>> obtenerTodos() {
        List<Profesor> profesores = profesorService.obtenerTodosLosProfesores();
        // aqui retorna las asignaturas y manda respuesta que se completo correctamente
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    // Se usa para enviar datos nuevos (crear)
    @PostMapping
    // RequestBody tooma el texto en JSON y lo transforma a un objeto java (en este caso Profesor)
    public ResponseEntity<Profesor> crearProfesor(@RequestBody Profesor profesor) {
        Profesor nuevoProfesor = profesorService.guardarProfesor(profesor);
        // se crea la nueva asignatura y se manda respuesta de que se creo
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }
}
