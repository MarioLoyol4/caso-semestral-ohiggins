package cl.duoc.colegio.academic.controller;

import cl.duoc.colegio.academic.model.Curso;
import cl.duoc.colegio.academic.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    @PostMapping
    public Curso guardar (@RequestBody Curso curso){
        return cursoRepository.save(curso);
    }
}
