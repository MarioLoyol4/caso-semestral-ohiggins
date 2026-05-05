package cl.duoc.colegio.academic.controller;


import cl.duoc.colegio.academic.model.Estudiante;
import cl.duoc.colegio.academic.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public List<Estudiante> listarTodos(){
        return estudianteRepository.findAll();
    }

    @PostMapping
    public Estudiante guardar(@RequestBody Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }
}
