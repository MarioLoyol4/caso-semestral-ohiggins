package fullstack.caso_semestral.academic.controllers;


import fullstack.caso_semestral.academic.models.Curso;
import fullstack.caso_semestral.academic.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RestController le dice a spring boot que esta clase recibe peticiones de internet y responde con JSON
@RestController
// RequestMapping define la ruta base
@RequestMapping("/api/cursos")
public class CursoController {

    // conecta automaticamente con CursoService
    @Autowired
    private CursoService cursoService;

    // Se usa para pedir datos (leer)
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerTodos() {
        List<Curso> cursos = cursoService.obtenerTodosLosCursos();
        // aqui se retornan los cursos y se manda una respuesta de que se completo la peticion correctamente
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    // Se usa para enviar datos nuevos (crear)
    @PostMapping
    // RequestBody toma el texto en JSON y lo transforma a un objeto java (en este caso Curso)
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.guardarCurso(curso);
        // Se crea el curso nuevo y se manda una respuesta de que se creo el curso correctamente
        return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
    }
}
